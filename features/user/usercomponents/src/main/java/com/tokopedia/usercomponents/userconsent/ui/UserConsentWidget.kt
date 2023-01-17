package com.tokopedia.usercomponents.userconsent.ui

import android.content.Context
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.common.wrapper.UserComponentsStateResult
import com.tokopedia.usercomponents.databinding.UiUserConsentBinding
import com.tokopedia.usercomponents.userconsent.analytics.UserConsentAnalytics
import com.tokopedia.usercomponents.userconsent.common.*
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.CHECKLIST
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.CONSENT_OPT_IN
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.MANDATORY
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.NO_CHECKLIST
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.OPTIONAL
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.TERM_CONDITION
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.TERM_CONDITION_POLICY
import com.tokopedia.usercomponents.userconsent.common.UserConsentType.*
import com.tokopedia.usercomponents.userconsent.di.DaggerUserConsentComponent
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.domain.submission.ConsentSubmissionParam
import com.tokopedia.usercomponents.userconsent.domain.submission.Purpose
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeAdapter
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeViewHolder
import javax.inject.Inject

class UserConsentWidget : FrameLayout,
    UserConsentPurposeViewHolder.UserConsentPurposeListener,
    UserConsentDescriptionDelegate
{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: UserConsentViewModel? = null

    @Inject
    lateinit var userConsentAnalytics: UserConsentAnalytics

    private val viewBinding: UiUserConsentBinding? =
        UiUserConsentBinding.inflate(
            LayoutInflater.from(context)
        ).also {
            addView(it.root)
        }

    private var lifecycleOwner: LifecycleOwner? = null
    private var consentCollectionParam: ConsentCollectionParam? = null
    private var submissionParam = ConsentSubmissionParam()
    private var collection: UserConsentCollectionDataModel.CollectionPointDataModel? = null
    private var isErrorGetConsent = false

    private var userConsentDescription: UserConsentDescription? = null
    private var userConsentPurposeAdapter: UserConsentPurposeAdapter? = null

    private var onCheckedChangeListener: (Boolean) -> Unit = {}
    private var onAllCheckBoxCheckedListener: (Boolean) -> Unit = {}
    private var onFailedGetCollectionListener: (Throwable) -> Unit = {}

    /** set Default State if user got error when trying to get data collection from BE **/
    var defaultTemplate: UserConsentType = NONE
    var tncPage = ""
    var privacyPage = ""

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) {
        setupView(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) {
        setupView(attributeSet)
    }

    private fun setupView(attributeSet: AttributeSet? = null) {
        initInjector()

        if (attributeSet != null) setAttributes(attributeSet)

        viewBinding?.apply {
            singleConsent.apply {
                checkboxPurposes.setOnCheckedChangeListener { buttonView, isChecked ->
                    collection?.purposes?.let {
                        userConsentAnalytics.trackOnPurposeCheck(isChecked, it)
                    }

                    onCheckedChangeListener.invoke(isChecked)
                }
            }

            multipleConsent.apply {
                userConsentPurposeAdapter = UserConsentPurposeAdapter(this@UserConsentWidget)
                recyclerPurposes.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    fun submitConsent() {
        collection?.purposes?.let {
            userConsentAnalytics.trackOnActionButtonClicked(it)
        }

        submissionParam.collectionId = consentCollectionParam?.collectionId.orEmpty()
        submissionParam.version = consentCollectionParam?.version.orEmpty()
        submissionParam.default = isErrorGetConsent
        submissionParam.dataElements = consentCollectionParam?.dataElements
        submissionParam.purposes.clear()
        collection?.purposes?.forEach {
            submissionParam.purposes.add(
                Purpose(
                    purposeID = it.id,
                    transactionType = CONSENT_OPT_IN,
                    version = it.version,
                )
            )
        }
        viewModel?.submitConsent(submissionParam)
    }

    private fun initInjector() {
        context?.let {
            DaggerUserConsentComponent.builder()
                .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
        }
    }

    private fun setAttributes(attributeSet: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.UserConsentWidget,
            0,
            0
        )

        val consentType = typedArray.getInt(R.styleable.UserConsentWidget_defaultTemplate, -1)
        defaultTemplate = when(consentType.toString()) {
            TNC_MANDATORY.value -> TNC_MANDATORY
            TNC_PRIVACY_MANDATORY.value -> TNC_PRIVACY_MANDATORY
            TNC_OPTIONAL.value -> TNC_OPTIONAL
            TNC_PRIVACY_OPTIONAL.value -> TNC_PRIVACY_OPTIONAL
            else -> NONE
        }

        typedArray.recycle()
    }

    private fun initViewModel(viewModelStoreOwner: ViewModelStoreOwner) {
        val viewModelProvider = ViewModelProvider(viewModelStoreOwner, viewModelFactory)
        viewModel = viewModelProvider.get(UserConsentViewModel::class.java)
    }

    private fun initObserver() {
        lifecycleOwner?.let {
            viewModel?.consentCollection?.observe(it) { result ->
                when(result) {
                    is UserComponentsStateResult.Loading -> {
                        setLoader(true)
                    }
                    is UserComponentsStateResult.Fail -> {
                        isErrorGetConsent = true
                        setLoader(false)
                        showError(result.error)
                    }
                    is UserComponentsStateResult.Success -> {
                        isErrorGetConsent = false
                        setLoader(false)
                        result.data?.let { data ->
                            collection = data.collectionPoints.first()
                            onSuccessGetConsentCollection()
                        }
                    }
                }
            }
        }
    }

    private fun onSuccessGetConsentCollection() {
        collection?.let {
            userConsentAnalytics.trackOnConsentView(it.purposes)
            userConsentDescription = UserConsentDescription(this, it)
        }

        renderView()
    }

    fun generatePayloadData(): String {
        collection?.purposes?.let {
            userConsentAnalytics.trackOnActionButtonClicked(it)
        }

        val purposes: MutableList<UserConsentPayload.PurposeDataModel> = mutableListOf()
        collection?.purposes?.forEach {
            purposes.add(UserConsentPayload.PurposeDataModel(
                it.id,
                it.version,
                collection?.consentType.orEmpty()
            ))
        }
        return UserConsentPayload(
            identifier = consentCollectionParam?.identifier.orEmpty(),
            collectionId = collection?.id.orEmpty(),
            dataElements = consentCollectionParam?.dataElements,
            default = isErrorGetConsent,
            purposes = purposes
        ).toString()
    }

    private fun renderView() {
        when {
            collection?.attributes?.collectionPointPurposeRequirement == MANDATORY -> {
                renderSinglePurpose()
            }

            collection?.attributes?.collectionPointPurposeRequirement == OPTIONAL &&
            collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST -> {
                renderMultiplePurpose()
            }
        }
    }

    private fun renderSinglePurpose() {
        var purposeText = ""
        if (collection?.purposes?.size.orZero() == NUMBER_ONE) {
            purposeText = collection?.purposes?.first()?.attribute?.uiName.orEmpty()
        } else {
            collection?.purposes?.forEachIndexed { index, purposeDataModel ->
                purposeText += when(index) {
                    (collection?.purposes?.size.orZero() - NUMBER_ONE) -> {
                        " & ${purposeDataModel.attribute.uiName}"
                    }

                    (collection?.purposes?.size.orZero() - NUMBER_TWO) -> {
                        purposeDataModel.attribute.uiName
                    }
                    else -> {
                        "${purposeDataModel.attribute.uiName}, "
                    }
                }
            }
        }

        viewBinding?.singleConsent?.apply {
            if (collection?.attributes?.collectionPointStatementOnlyFlag == NO_CHECKLIST) {
                checkboxPurposes.hide()
                iconMandatoryInfo.show()
            } else if (collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST) {
                checkboxPurposes.show()
                iconMandatoryInfo.hide()
            }

            if (collection?.attributes?.policyNoticeType == TERM_CONDITION) {
                descriptionPurposes.text = userConsentDescription?.generateTermConditionSinglePurposeText(
                    collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST,
                    purposeText
                )
            } else if (collection?.attributes?.policyNoticeType == TERM_CONDITION_POLICY) {
                descriptionPurposes.text = userConsentDescription?.generateTermConditionPolicySinglePurposeText(
                    collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST,
                    purposeText
                )
            }

            descriptionPurposes.movementMethod = LinkMovementMethod.getInstance()
        }?.root?.show()
    }

    private fun renderMultiplePurpose() {
        if (collection?.purposes?.size.orZero() > NUMBER_ONE) {
            viewBinding?.multipleConsent?.apply {
                if (collection?.attributes?.policyNoticeType == TERM_CONDITION) {
                    textMainDescription.text = userConsentDescription?.generateTermConditionMultipleOptionalPurposeText()
                } else if (collection?.attributes?.policyNoticeType == TERM_CONDITION_POLICY) {
                    textMainDescription.text = userConsentDescription?.generateTermConditionPolicyMultipleOptionalPurposeText()
                }

                textMainDescription.movementMethod = LinkMovementMethod.getInstance()
                recyclerPurposes.adapter = userConsentPurposeAdapter
                userConsentPurposeAdapter?.clearAllItems()
                collection?.purposes?.forEach {
                    userConsentPurposeAdapter?.addItem(UserConsentPurposeUiModel(it))
                }
            }?.root?.show()
        }
    }

    private fun renderDefaultTemplate(consentType: UserConsentType) {
        userConsentDescription = UserConsentDescription(this)

        viewBinding?.apply {
            setLoader(false)

            multipleConsent.root.hide()
            consentError.hide()

            singleConsent.apply {
                when(consentType) {
                    TNC_MANDATORY -> {
                        iconMandatoryInfo.hide()
                        checkboxPurposes.show()
                        descriptionPurposes.text = userConsentDescription?.generateDefaultTemplateTnc(true, tncPage)
                    }
                    TNC_PRIVACY_MANDATORY -> {
                        iconMandatoryInfo.hide()
                        checkboxPurposes.show()
                        descriptionPurposes.text = userConsentDescription?.generateDefaultTemplateTncPolicy(true, tncPage, privacyPage)
                    }
                    TNC_OPTIONAL -> {
                        iconMandatoryInfo.show()
                        checkboxPurposes.hide()
                        descriptionPurposes.text = userConsentDescription?.generateDefaultTemplateTnc(false, tncPage)
                    }
                    TNC_PRIVACY_OPTIONAL -> {
                        iconMandatoryInfo.show()
                        checkboxPurposes.hide()
                        descriptionPurposes.text = userConsentDescription?.generateDefaultTemplateTncPolicy(false, tncPage, privacyPage)
                    }
                    NONE -> {
                        showError(Throwable("default template not found"))
                        root.hide()
                        descriptionPurposes.text = ""
                    }
                }

                descriptionPurposes.movementMethod = LinkMovementMethod.getInstance()
            }.root.show()
        }
    }

    private fun setLoader(isLoading: Boolean) {
        viewBinding?.apply {
            if (isLoading) {
                consentLoader.root.show()

                singleConsent.root.hide()
                multipleConsent.root.hide()
                consentError.hide()
            } else {
                consentLoader.root.hide()
            }
        }
    }

    private fun showError(throwable: Throwable) {
        if (defaultTemplate == NONE) {
            viewBinding?.consentError?.apply {
                title?.text = resources.getString(R.string.usercomponents_failed_load_data)
                refreshBtn?.setOnClickListener {
                    consentCollectionParam?.let { param ->
                        viewModel?.getConsentCollection(param)
                    }
                }
            }?.show()

            onFailedGetCollectionListener.invoke(throwable)
        } else {
            renderDefaultTemplate(defaultTemplate)
        }
    }

    override fun onCheckedChange(
        isChecked: Boolean,
        purposeDataModel: UserConsentCollectionDataModel.CollectionPointDataModel.PurposeDataModel,
    ) {
        onCheckedChangeListener.invoke(isChecked)
        userConsentAnalytics.trackOnPurposeCheckOnOptional(isChecked, purposeDataModel)

        val isAllChecked = userConsentPurposeAdapter?.listCheckBoxView?.all {
            it.isChecked
        }

        onAllCheckBoxCheckedListener.invoke(isAllChecked == true)
    }

    override fun invalidate() {
        super.invalidate()

        viewBinding?.singleConsent?.apply {
            checkboxPurposes.isChecked = false
        }

        userConsentPurposeAdapter?.clearAllItems()
    }

    override val textTermCondition: String
        get() = context?.resources?.getString(R.string.user_consent_term_condition).orEmpty()
    override val textPolicy: String
        get() = context?.resources?.getString(R.string.user_consent_policy).orEmpty()
    override val textAgreementSingleTncOptional: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_single_purpose_term_condition).orEmpty()
    override val textAgreementSingleTncMandatory: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_single_purpose_term_condition_mandatory).orEmpty()
    override val textAgreementSingleTncPolicyOptional: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_single_purpose_term_condition_policy).orEmpty()
    override val textAgreementSingleTncPolicyMandatory: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_single_purpose_term_condition_policy_mandatory).orEmpty()
    override val textAgreementMultiTnc: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_multiple_purpose_term_condition_optional).orEmpty()
    override val textAgreementMultiTncPolicy: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_multiple_purpose_term_condition_policy_optional).orEmpty()
    override val textAgreementDefaultTncMandatory: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_default_term_condition_mandatory).orEmpty()
    override val textAgreementDefaultTncOptional: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_default_term_condition_optional).orEmpty()
    override val textAgreementDefaultTncPolicyMandatory: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_default_term_condition_policy_mandatory).orEmpty()
    override val textAgreementDefaultTncPolicyOptional: String
        get() = context?.resources?.getString(R.string.user_consent_agreement_default_term_condition_policy_optional).orEmpty()
    override val unifyG500: Int
        get() = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)

    override fun openConsentPageDetail(pageId: String, type: String, tab: String) {
        val urlPage = String.format(UserConsentConst.URL_CONSENT_DETAIL, pageId, type, tab)
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, urlPage)
        context.startActivity(intent)
    }

    override fun openDefaultConsentPageDetail(url: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.WEBVIEW, url)
        context.startActivity(intent)
    }

    fun load(
        lifecycleOwner: LifecycleOwner,
        viewModelStoreOwner: ViewModelStoreOwner,
        consentCollectionParam: ConsentCollectionParam
    ) {
        this.lifecycleOwner = lifecycleOwner
        this.consentCollectionParam = consentCollectionParam

        invalidate()
        initViewModel(viewModelStoreOwner)
        initObserver()
        viewModel?.getConsentCollection(consentCollectionParam)
    }

    fun onDestroy() {
        lifecycleOwner?.let {
            viewModel?.consentCollection?.removeObservers(it)
        }

        lifecycleOwner = null
    }
    fun setOnCheckedChangeListener(listener: (Boolean) -> Unit) {
        onCheckedChangeListener = listener
    }

    fun setOnAllCheckBoxCheckedListener(listener: (Boolean) -> Unit) {
        onAllCheckBoxCheckedListener = listener
    }

    fun setOnFailedGetCollectionListener(listener: (Throwable) -> Unit) {
        onFailedGetCollectionListener = listener
    }

    companion object {
        private const val NUMBER_ONE = 1
        private const val NUMBER_TWO = 2
    }
}

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
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.usercomponents.R
import com.tokopedia.usercomponents.common.wrapper.UserComponentsStateResult
import com.tokopedia.usercomponents.databinding.UiUserConsentBinding
import com.tokopedia.usercomponents.userconsent.analytics.UserConsentAnalytics
import com.tokopedia.usercomponents.userconsent.common.*
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.CHECKLIST
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.CONSENT_OPT_IN
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.CONSENT_OPT_OUT
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.MANDATORY
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.NO_CHECKLIST
import com.tokopedia.usercomponents.userconsent.common.UserConsentConst.OPTIONAL
import com.tokopedia.usercomponents.userconsent.common.UserConsentType.*
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.domain.submission.ConsentSubmissionParam
import com.tokopedia.usercomponents.userconsent.domain.submission.ConsentSubmissionResponse
import com.tokopedia.usercomponents.userconsent.domain.submission.Purpose
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeAdapter
import com.tokopedia.usercomponents.userconsent.ui.adapter.UserConsentPurposeViewHolder
import javax.inject.Inject

class UserConsentWidget :
    FrameLayout,
    UserConsentPurposeViewHolder.UserConsentPurposeListener,
    UserConsentDescriptionDelegate {

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
    private var collection: CollectionPointDataModel? = null
    private var isErrorGetConsent = false
    private var needConsent: Boolean? = null
    private var isConsentTypeInfo: Boolean = false

    private var userConsentDescription: UserConsentDescription? = null
    private var userConsentPurposeAdapter: UserConsentPurposeAdapter? = null

    private var onCheckedChangeListener: (Boolean) -> Unit = {}
    private var onAllCheckBoxCheckedListener: (Boolean) -> Unit = {}
    private var onDetailConsentListener: (Boolean, ConsentType) -> Unit = { _, _ -> }
    private var onSubmitSuccessListener: (ConsentSubmissionResponse?) -> Unit = {}
    private var onSubmitErrorListener: (Throwable) -> Unit = {}
    private var onSubmitLoadingListener: () -> Unit = {}
    private var onFailedGetCollectionListener: (Throwable) -> Unit = {}

    /** set Default State if user got error when trying to get data collection from BE **/
    var defaultTemplate: UserConsentType = NONE
    var hideWhenAlreadySubmittedConsent: Boolean = false
    var tncPage = ""
    var privacyPage = ""

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setupView(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        setupView(attributeSet)
    }

    private fun setupView(attributeSet: AttributeSet? = null) {
        initInjector()

        if (attributeSet != null) setAttributes(attributeSet)

        viewBinding?.apply {
            singleConsent.apply {
                checkboxPurposes.setOnCheckedChangeListener { _, isChecked ->
                    collection?.purposes?.let {
                        userConsentAnalytics.trackOnPurposeCheck(
                            isChecked = isChecked,
                            purposes = it,
                            collectionId = consentCollectionParam?.collectionId.orEmpty()
                        )
                        it.forEach { purposeDataModel ->
                            purposeDataModel.transactionType = if (isChecked) CONSENT_OPT_IN else CONSENT_OPT_OUT
                        }
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
            userConsentAnalytics.trackOnActionButtonClicked(
                purposes = it,
                collectionId = consentCollectionParam?.collectionId.orEmpty()
            )
        }

        if (needConsent != false) {
            submissionParam.collectionId = consentCollectionParam?.collectionId.orEmpty()
            submissionParam.version = consentCollectionParam?.version.orEmpty()
            submissionParam.default = isErrorGetConsent
            submissionParam.dataElements = consentCollectionParam?.dataElements.orEmpty().toMutableList()
            submissionParam.purposes.clear()
            collection?.purposes?.forEach {
                submissionParam.purposes.add(
                    Purpose(
                        purposeID = it.id,
                        /*
                        * default value of transactionType is OPT_OUT, because the first time show checkbox always uncheck
                        * specially for consentTypeInfo (that no checkbox show) the value must be OPT_IN.
                        */
                        transactionType =
                        if (isConsentTypeInfo) {
                            CONSENT_OPT_IN
                        } else {
                            it.transactionType
                        },
                        version = it.version,
                        dataElementType = it.attribute.dataElementType
                    )
                )
            }
            viewModel?.submitConsent(submissionParam)
        }
    }

    private fun initInjector() {
        context?.let {
            UserConsentComponentProvider.getUserConsentComponent(it)?.inject(this)
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
        defaultTemplate = when (consentType.toString()) {
            TNC_MANDATORY.value -> TNC_MANDATORY
            TNC_PRIVACY_MANDATORY.value -> TNC_PRIVACY_MANDATORY
            TNC_OPTIONAL.value -> TNC_OPTIONAL
            TNC_PRIVACY_OPTIONAL.value -> TNC_PRIVACY_OPTIONAL
            else -> NONE
        }
        hideWhenAlreadySubmittedConsent = typedArray.getBoolean(R.styleable.UserConsentWidget_hide_when_already_submitted_consent, false)

        typedArray.recycle()
    }

    private fun initViewModel(viewModelStoreOwner: ViewModelStoreOwner) {
        val viewModelProvider = ViewModelProvider(viewModelStoreOwner, viewModelFactory)
        viewModel = viewModelProvider.get(UserConsentViewModel::class.java)
    }

    private fun initObserver() {
        lifecycleOwner?.let {
            viewModel?.consentCollection?.observe(it) { result ->
                when (result) {
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
                            needConsent = collection?.needConsent
                            val consentType = getConsentType()
                            if (needConsent == false) {
                                this.hide()
                            } else if (consentType == null) {
                                showError()
                            } else {
                                onSuccessGetConsentCollection(consentType)
                            }
                            consentType?.apply {
                                onDetailConsentListener.invoke(needConsent != false, consentType)
                            }
                        }
                    }
                }
            }

            viewModel?.submitResult?.observe(it) { result ->
                when (result) {
                    is UserComponentsStateResult.Success -> {
                        onSubmitSuccessListener(result.data)
                    }
                    is UserComponentsStateResult.Fail -> {
                        onSubmitErrorListener(result.error)
                    }
                    is UserComponentsStateResult.Loading -> {
                        onSubmitLoadingListener()
                    }
                }
            }
        }
    }

    private fun getConsentType(): ConsentType? {
        return if (collection?.attributes?.collectionPointPurposeRequirement == MANDATORY) {
            when (collection?.attributes?.collectionPointStatementOnlyFlag) {
                NO_CHECKLIST -> {
                    ConsentType.SingleInfo()
                }
                CHECKLIST -> {
                    ConsentType.SingleChecklist()
                }
                else -> null
            }
        } else if (collection?.attributes?.collectionPointPurposeRequirement == OPTIONAL &&
            collection?.attributes?.collectionPointStatementOnlyFlag == CHECKLIST
        ) {
            ConsentType.MultipleChecklist()
        } else {
            null
        }
    }

    private fun onSuccessGetConsentCollection(consentType: ConsentType?) {
        collection?.let {
            userConsentAnalytics.trackOnConsentView(
                purposes = it.purposes,
                collectionId = consentCollectionParam?.collectionId.orEmpty()
            )
            userConsentDescription = UserConsentDescription(
                delegate = this,
                collectionDataModel = it,
                collectionId = consentCollectionParam?.collectionId.orEmpty()
            )
        }

        renderView(consentType)
    }

    fun generatePayloadData(): String {
        collection?.purposes?.let {
            userConsentAnalytics.trackOnActionButtonClicked(
                purposes = it,
                collectionId = consentCollectionParam?.collectionId.orEmpty()
            )
        }

        return if (needConsent == false) {
            ""
        } else {
            val purposes: MutableList<UserConsentPayload.PurposeDataModel> = mutableListOf()
            collection?.purposes?.forEach {
                purposes.add(
                    UserConsentPayload.PurposeDataModel(
                        purposeId = it.id,
                        version = it.version,
                    /*
                    * default value of transactionType is OPT_OUT, because the first time show checkbox always uncheck
                    * specially for consentTypeInfo (that no checkbox show) the value must be OPT_IN.
                    */
                        transactionType =
                        if (isConsentTypeInfo) {
                            CONSENT_OPT_IN
                        } else {
                            it.transactionType
                        },
                        dataElementType = it.attribute.dataElementType
                    )
                )
            }
            val dataElements = mutableMapOf<String, String>()
            consentCollectionParam?.dataElements?.forEach { it ->
                dataElements[it.elementName] = it.elementValue
            }
            UserConsentPayload(
                identifier = consentCollectionParam?.identifier.orEmpty(),
                collectionId = collection?.id.orEmpty(),
                dataElements = dataElements,
                default = isErrorGetConsent,
                purposes = purposes
            ).toString()
        }
    }

    private fun renderView(consentType: ConsentType?) {
        isConsentTypeInfo = consentType is ConsentType.SingleInfo
        when (consentType) {
            is ConsentType.SingleInfo -> {
                renderSinglePurposeInfo()
            }
            is ConsentType.SingleChecklist -> {
                renderSinglePurposeChecklist()
            }
            is ConsentType.MultipleChecklist -> {
                renderMultiplePurpose()
            }
            else -> {
                // no op
            }
        }
    }

    private fun renderSinglePurposeInfo() {
        viewBinding?.singleConsent?.apply {
            checkboxPurposes.hide()
            iconMandatoryInfo.show()
        }
        renderSinglePurpose()
    }

    private fun renderSinglePurposeChecklist() {
        viewBinding?.singleConsent?.apply {
            checkboxPurposes.show()
            iconMandatoryInfo.hide()
        }
        renderSinglePurpose()
    }

    private fun renderSinglePurpose() {
        viewBinding?.singleConsent?.apply {
            collection?.attributes?.statementWording?.apply {
                descriptionPurposes.text = userConsentDescription?.generateDescriptionSpannableText(this)
            }

            descriptionPurposes.movementMethod = LinkMovementMethod.getInstance()
        }?.root?.show()
    }

    private fun renderMultiplePurpose() {
        if (collection?.purposes?.size.orZero() > NUMBER_ONE) {
            viewBinding?.multipleConsent?.apply {
                collection?.attributes?.statementWording?.apply {
                    textMainDescription.text = userConsentDescription?.generateDescriptionSpannableText(this)
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
        userConsentDescription = UserConsentDescription(
            delegate = this,
            collectionId = consentCollectionParam?.collectionId.orEmpty()
        )

        viewBinding?.apply {
            setLoader(false)

            multipleConsent.root.hide()
            consentError.hide()

            singleConsent.apply {
                when (consentType) {
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

    private fun showError(throwable: Throwable? = null) {
        if (defaultTemplate == NONE) {
            viewBinding?.consentError?.apply {
                title?.text = resources.getString(R.string.usercomponents_failed_load_data)
                refreshBtn?.setOnClickListener {
                    consentCollectionParam?.let { param ->
                        viewModel?.getConsentCollection(param, hideWhenAlreadySubmittedConsent)
                    }
                }
            }?.show()

            throwable?.let {
                onFailedGetCollectionListener.invoke(throwable)
            }
        } else {
            renderDefaultTemplate(defaultTemplate)
        }
    }

    override fun onCheckedChange(
        isChecked: Boolean,
        purposeDataModel: PurposeDataModel
    ) {
        onCheckedChangeListener.invoke(isChecked)
        userConsentAnalytics.trackOnPurposeCheckOnOptional(
            isChecked = isChecked,
            purposes = purposeDataModel,
            collectionId = consentCollectionParam?.collectionId.orEmpty()
        )

        val isAllChecked = userConsentPurposeAdapter?.listCheckBoxView?.all {
            it.isChecked
        }

        collection?.purposes?.forEach {
            if (it.id == purposeDataModel.id) {
                it.transactionType = if (isChecked) CONSENT_OPT_IN else CONSENT_OPT_OUT
            }
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
        get() = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)

    override fun openWebview(url: String) {
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
        viewModel?.getConsentCollection(consentCollectionParam, hideWhenAlreadySubmittedConsent)
    }

    fun onDestroy() {
        removeConsentCollectionObserver()
        lifecycleOwner = null
    }

    fun removeConsentCollectionObserver() {
        lifecycleOwner?.let {
            viewModel?.consentCollection?.removeObservers(it)
        }
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

    fun setOnDetailConsentListener(listener: (Boolean, ConsentType) -> Unit) {
        onDetailConsentListener = listener
    }

    fun setSubmitResultListener(
        onSuccess: ((ConsentSubmissionResponse?) -> Unit),
        onError: ((Throwable) -> Unit),
        onLoading: (() -> Unit)
    ) {
        onSubmitSuccessListener = onSuccess
        onSubmitErrorListener = onError
        onSubmitLoadingListener = onLoading
    }

    fun isNeedConsent(): Boolean {
        return needConsent != false
    }

    companion object {
        private const val NUMBER_ONE = 1
        private const val NUMBER_TWO = 2
    }
}

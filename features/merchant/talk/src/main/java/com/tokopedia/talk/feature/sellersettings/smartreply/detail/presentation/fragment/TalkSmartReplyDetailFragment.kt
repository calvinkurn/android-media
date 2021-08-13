package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.sellersettings.smartreply.common.util.TalkSmartReplyConstants
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.di.DaggerTalkSmartReplyDetailComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.di.TalkSmartReplyDetailComponent
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.viewmodel.TalkSmartReplyDetailViewModel
import com.tokopedia.talk.R
import com.tokopedia.talk.common.utils.FirebaseLogger
import com.tokopedia.talk.common.utils.UpdateTrackerListener
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController.setNavigationResult
import com.tokopedia.talk.feature.sellersettings.common.util.TalkSellerSettingsConstants
import com.tokopedia.talk.feature.sellersettings.common.util.UserSessionListener
import com.tokopedia.talk.feature.sellersettings.smartreply.common.data.SmartReplyDataWrapper
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.analytics.TalkSmartReplyDetailTracking
import com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.widget.TalkSmartReplyDetailCard
import com.tokopedia.talk.feature.sellersettings.template.presentation.fragment.TalkTemplateBottomsheet
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.selectioncontrol.SwitchUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TalkSmartReplyDetailFragment : BaseDaggerFragment(), HasComponent<TalkSmartReplyDetailComponent> {

    @Inject
    lateinit var viewModel: TalkSmartReplyDetailViewModel

    private var isTemplateEdited = false

    private var toaster: Snackbar? = null
    private var talkSmartReplyDetailSubtitle: Typography? = null
    private var talkSmartReplySwitch: SwitchUnify? = null
    private var talkSmartReplyDetailCardContainer: ConstraintLayout? = null
    private var talkSmartReplyDetailCard: TalkSmartReplyDetailCard? = null
    private var talkSmartReplyDetailAvailableStockTextArea: TextAreaUnify? = null
    private var talkSmartReplyDetailUnavailableStockTextArea: TextAreaUnify? = null
    private var talkSmartReplyDetailSubmitButton: UnifyButton? = null

    override fun getComponent(): TalkSmartReplyDetailComponent? {
        return activity?.run {
            DaggerTalkSmartReplyDetailComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val cacheManagerId = TalkSmartReplyDetailFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = context?.let { SaveInstanceCacheManager(it, cacheManagerId) }

            saveInstanceCacheManager?.run {
                val smartReplyDataWrapper = get<SmartReplyDataWrapper>(TalkSmartReplyConstants.SMART_REPLY_DATA_WRAPPER, SmartReplyDataWrapper::class.java)
                viewModel.isSmartReplyOn = smartReplyDataWrapper?.isSmartReplyOn ?: false
                viewModel.updateIsSwitchActive(viewModel.isSmartReplyOn)
                viewModel.messageReady = smartReplyDataWrapper?.messageReady
                        ?: getString(R.string.smart_reply_available_stock_text_area_default_text)
                viewModel.messageNotReady = smartReplyDataWrapper?.messageNotReady
                        ?: getString(R.string.smart_reply_unavailable_stock_text_area_default_text)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_smart_reply_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewReferences(view)
        viewModel.initMessages()
        setCardText()
        initSwitchState()
        initTextArea()
        setDescriptionText()
        setToolbarTitle()
        setupOnBackPressed()
        initButton()
        observeSetSmartReplyResult()
        observeButtonState()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun setDescriptionText() {
        talkSmartReplyDetailSubtitle?.text = context?.let { HtmlLinkHelper(it, getString(R.string.smart_reply_settings_description)).spannedString }
    }

    private fun setToolbarTitle() {
        val toolbar = activity?.findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)
        toolbar?.setTitle(R.string.title_smart_reply_detail_page)
    }

    private fun initSwitchState() {
        talkSmartReplySwitch?.isChecked = viewModel.isSmartReplyOn
        talkSmartReplySwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            updateSwitchState(isChecked)
            viewModel.isSmartReplyOn = isChecked
            viewModel.setSmartReply()
            viewModel.updateIsSwitchActive(isChecked)
        }
    }

    private fun setCardData() {
        talkSmartReplyDetailCardContainer?.show()
        talkSmartReplyDetailCard?.setData(viewModel.shopName, viewModel.shopAvatar)
    }

    private fun setCardText() {
        talkSmartReplyDetailCard?.setSmartReply(viewModel.messageReady)
    }

    private fun initTextArea() {
        talkSmartReplyDetailAvailableStockTextArea?.textAreaInput?.apply {
            setText(viewModel.messageReady)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.messageReady = s?.let {
                        it.toString()
                    } ?: ""
                    viewModel.updateMessageChanged(s.toString(), true)
                    setCardText()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No Op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No Op
                }
            })
        }
        talkSmartReplyDetailUnavailableStockTextArea?.textAreaInput?.apply {
            setText(viewModel.messageNotReady)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.messageNotReady = s?.let { it.toString() } ?: ""
                    viewModel.updateMessageChanged(s.toString(), false)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No Op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No Op
                }
            })
        }
    }

    private fun observeSetSmartReplyResult() {
        viewModel.setSmartReplyResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessSetSmartReplyResult()
                is Fail -> onFailSetSmartReply(it.throwable)
            }
        })
    }

    private fun observeButtonState() {
        viewModel.buttonState.observe(viewLifecycleOwner, Observer {
            when {
                // Smart Reply active & text changed
                (it.isReadyTextChanged || it.isNotReadyTextChanged) && it.isSwitchActive -> {
                    talkSmartReplyDetailSubmitButton?.apply {
                        isEnabled = true
                        show()
                    }
                    enableTextAreas()
                    setCardData()
                }
                // Smart Reply active & text not changed
                !it.isReadyTextChanged && !it.isNotReadyTextChanged && it.isSwitchActive -> {
                    talkSmartReplyDetailSubmitButton?.apply {
                        isEnabled = false
                        show()
                    }
                    enableTextAreas()
                    setCardData()
                }
                //Smart Reply inactive
                !it.isSwitchActive -> {
                    talkSmartReplyDetailSubmitButton?.hide()
                    disableTextAreas()
                }
            }
        })
    }

    private fun onSuccessSetSmartReplyResult() {
        when {
            viewModel.isSmartReplyOn && talkSmartReplyDetailSubmitButton?.isEnabled == false -> {
                showToaster(getString(R.string.smart_reply_success_activate), false)
            }
            !viewModel.isSmartReplyOn -> {
                showToaster(getString(R.string.smart_reply_success_deactivate), false)
            }
            viewModel.isSmartReplyOn && talkSmartReplyDetailSubmitButton?.isEnabled == true -> {
                showToaster(getString(R.string.smart_reply_success_saved), false)
            }
        }
        isTemplateEdited = true
    }

    private fun showToaster(successMessage: String, isError: Boolean) {
        val toasterType = if (isError) Toaster.TYPE_ERROR else Toaster.TYPE_NORMAL
        view?.let {
            this.toaster = Toaster.build(it, successMessage, Snackbar.LENGTH_LONG, toasterType, getString(R.string.talk_ok))
            toaster?.let { toaster ->
                if (toaster.isShownOrQueued) {
                    return
                }
                toaster.show()
            }
        }
    }

    private fun initButton() {
        talkSmartReplyDetailSubmitButton?.setOnClickListener {
            TalkSmartReplyDetailTracking.eventClickSave(getShopId(), getUserId())
            saveSmartReplySettings()
        }
    }

    private fun setFragmentResultWithBundle(requestValue: String) {
        arguments?.let {
            if (isTemplateEdited) {
                val bundle = Bundle().apply {
                    putString(TalkSellerSettingsConstants.KEY_ACTION, requestValue)
                }
                setNavigationResult(bundle, TalkTemplateBottomsheet.REQUEST_KEY)
            }
            findNavController().navigateUp()
        }
    }

    private fun saveSmartReplySettings() {
        viewModel.setSmartReplyTemplate()
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setFragmentResultWithBundle(TalkSellerSettingsConstants.VALUE_ADD_EDIT)
            }
        })
    }

    private fun disableTextAreas() {
        talkSmartReplyDetailUnavailableStockTextArea?.textAreaInput?.isEnabled = false
        talkSmartReplyDetailAvailableStockTextArea?.textAreaInput?.isEnabled = false
    }

    private fun enableTextAreas() {
        talkSmartReplyDetailUnavailableStockTextArea?.textAreaInput?.isEnabled = true
        talkSmartReplyDetailAvailableStockTextArea?.textAreaInput?.isEnabled = true
    }

    private fun bindViewReferences(view: View) {
        talkSmartReplyDetailSubtitle = view.findViewById(R.id.talkSmartReplyDetailSubtitle)
        talkSmartReplySwitch = view.findViewById(R.id.talkSmartReplySwitch)
        talkSmartReplyDetailCardContainer = view.findViewById(R.id.talkSmartReplyDetailCardContainer)
        talkSmartReplyDetailCard = view.findViewById(R.id.talkSmartReplyDetailCard)
        talkSmartReplyDetailAvailableStockTextArea = view.findViewById(R.id.talkSmartReplyDetailAvailableStockTextArea)
        talkSmartReplyDetailUnavailableStockTextArea = view.findViewById(R.id.talkSmartReplyDetailUnavailableStockTextArea)
        talkSmartReplyDetailSubmitButton = view.findViewById(R.id.talkSmartReplyDetailSubmitButton)
    }

    private fun getUserId(): String {
        return (activity as? UserSessionListener)?.getUserId() ?: ""
    }

    private fun getShopId(): String {
        return (activity as? UserSessionListener)?.getShopId() ?: ""
    }

    private fun onFailSetSmartReply(throwable: Throwable) {
        logException(throwable)
        showToaster(throwable.message ?: getString(R.string.inbox_toaster_connection_error), true)
    }

    private fun logException(throwable: Throwable) {
        FirebaseLogger.logError(throwable)
    }

    private fun updateSwitchState(isChecked: Boolean) {
        (activity as? UpdateTrackerListener)?.updateSmartReplyTracker(isChecked)
    }
}
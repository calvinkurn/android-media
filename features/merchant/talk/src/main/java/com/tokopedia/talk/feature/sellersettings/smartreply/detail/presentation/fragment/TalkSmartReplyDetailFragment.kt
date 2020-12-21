package com.tokopedia.talk.feature.sellersettings.smartreply.detail.presentation.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
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
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController.setNavigationResult
import com.tokopedia.talk.feature.sellersettings.common.util.TalkSellerSettingsConstants
import com.tokopedia.talk.feature.sellersettings.smartreply.common.data.SmartReplyDataWrapper
import com.tokopedia.talk.feature.sellersettings.template.presentation.fragment.TalkEditTemplateFragment
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_smart_reply_detail.*
import javax.inject.Inject

class TalkSmartReplyDetailFragment : BaseDaggerFragment(), HasComponent<TalkSmartReplyDetailComponent> {

    @Inject
    lateinit var viewModel: TalkSmartReplyDetailViewModel

    private var toaster: Snackbar? = null
    private var isTemplateEdited = false

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
                viewModel.messageReady = smartReplyDataWrapper?.messageReady ?: getString(R.string.smart_reply_available_stock_text_area_default_text)
                viewModel.messageNotReady = smartReplyDataWrapper?.messageNotReady ?: getString(R.string.smart_reply_unavailable_stock_text_area_default_text)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_smart_reply_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwitchState()
        initTextArea()
        setDescriptionText()
        setToolbarTitle()
        setupOnBackPressed()
        initButton()
        observeSetSmartReplyResult()
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        component?.inject(this)
    }

    private fun setDescriptionText() {
        talkSmartReplyDetailSubtitle.text = context?.let { HtmlLinkHelper(it, getString(R.string.smart_reply_settings_description)).spannedString }
    }

    private fun setToolbarTitle() {
        val toolbar = activity?.findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)
        toolbar?.setTitle(R.string.title_smart_reply_detail_page)
    }

    private fun initSwitchState() {
        talkSmartReplySwitch.isChecked = viewModel.isSmartReplyOn
        talkSmartReplySwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.isSmartReplyOn = isChecked
            viewModel.setSmartReply()
            if(isChecked) {
                talkSmartReplyDetailSubmitButton.apply {
                    show()
                    isEnabled = isChecked
                }
            } else {
                setCardData()
            }
        }
        setCardData()
    }

    private fun setCardData() {
        talkSmartReplyDetailCardContainer.show()
        talkSmartReplyDetailCard.setData(viewModel.shopName, viewModel.shopAvatar)
    }

    private fun initTextArea() {
        talkSmartReplyDetailAvailableStockTextArea.textAreaInput.apply {
            setText(viewModel.messageReady)
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.messageReady = s?.let { it.toString() } ?: ""
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No Op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No Op
                }
            })
        }
        talkSmartReplyDetailUnavailableStockTextArea.textAreaInput.apply {
            setText(viewModel.messageNotReady)
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    viewModel.messageNotReady = s?.let { it.toString() } ?: ""
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
            when(it) {
                is Success -> {
                    when {
                        viewModel.isSmartReplyOn && !talkSmartReplyDetailSubmitButton.isEnabled -> {
                            showToaster(getString(R.string.smart_reply_success_activate), false)
                        }
                        !viewModel.isSmartReplyOn -> {
                            showToaster(getString(R.string.smart_reply_success_deactivate), false)
                        }
                        viewModel.isSmartReplyOn && talkSmartReplyDetailSubmitButton.isEnabled -> {
                            showToaster(getString(R.string.smart_reply_success_saved), false)
                        }
                    }
                    isTemplateEdited = true
                }
                is Fail -> {
                    showToaster(it.throwable.message ?: getString(R.string.inbox_toaster_connection_error), true)
                }
            }
        })
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
        talkSmartReplyDetailSubmitButton.apply {
            setOnClickListener {
                saveSmartReplySettings()
            }
            if(viewModel.isSmartReplyOn) {
                show()
            }
        }
    }

    private fun setFragmentResultWithBundle(requestValue: String) {
        arguments?.let {
            if(isTemplateEdited) {
                val bundle = Bundle().apply {
                    putString(TalkSellerSettingsConstants.KEY_ACTION, requestValue)
                }
                setNavigationResult(bundle, TalkEditTemplateFragment.REQUEST_KEY)
            }
            findNavController().navigateUp()
        }
    }

    private fun saveSmartReplySettings() {
        viewModel.setSmartReplyTemplate()
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setFragmentResultWithBundle(TalkSellerSettingsConstants.VALUE_ADD_EDIT)
            }
        })
    }
}
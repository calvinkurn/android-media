package com.tokopedia.talk.feature.sellersettings.template.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.talk.R
import com.tokopedia.talk.common.utils.FirebaseLogger
import com.tokopedia.talk.feature.sellersettings.common.util.UserSessionListener
import com.tokopedia.talk.feature.sellersettings.template.analytics.TalkTemplateTracking
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateDataWrapper
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateMutationResults
import com.tokopedia.talk.feature.sellersettings.template.di.DaggerTalkTemplateComponent
import com.tokopedia.talk.feature.sellersettings.template.di.TalkTemplateComponent
import com.tokopedia.talk.feature.sellersettings.template.presentation.listener.TalkTemplateBottomSheetListener
import com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel.TalkEditTemplateViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import javax.inject.Inject

class TalkTemplateBottomsheet : BottomSheetUnify(), HasComponent<TalkTemplateComponent> {

    companion object {
        const val REQUEST_KEY = "talk_template_request"
        const val KEY_TEMPLATE = "template"
        const val BOLD_REOURCE = "RobotoBold.ttf"
        const val TEXT_LIMIT = 200
        fun createNewInstance(context: Context, title: String): TalkTemplateBottomsheet {
            return TalkTemplateBottomsheet().apply {
                val view = View.inflate(context, R.layout.fragment_talk_edit_template, null)
                setTitle(title)
                setChild(view)
            }
        }
    }

    @Inject
    lateinit var viewModel: TalkEditTemplateViewModel

    private var isSeller: Boolean = false
    private var index: Int = -1
    private var templateText: String = ""
    private var toaster: Snackbar? = null
    private var isEditMode: Boolean = false
    private var allowDelete: Boolean = false
    private var cacheManagerId = ""
    private var talkTemplateBottomSheetListener: TalkTemplateBottomSheetListener? = null
    private var talkEditTemplateEditText: EditText? = null
    private var talkEditTemplateSaveButton: UnifyButton? = null

    fun setCacheManagerId(cacheId: String) {
        cacheManagerId = cacheId
    }

    fun setListener(listener: TalkTemplateBottomSheetListener) {
        talkTemplateBottomSheetListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
    }

    override fun getComponent(): TalkTemplateComponent? {
        return activity?.run {
            DaggerTalkTemplateComponent
                    .builder()
                    .talkComponent(TalkInstance.getComponent(application))
                    .build()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromCacheManager()
        observeTemplateMutation()
        initButton(view)
        if (isEditMode && allowDelete) {
            context?.let {
                setAction(getString(R.string.template_list_delete)) {
                    deleteTemplate()
                }
                bottomSheetAction.apply {
                    setTextColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
                    this.typeface = com.tokopedia.unifyprinciples.getTypeface(context, BOLD_REOURCE)
                }
            }
        }
        setupEditText(view)
    }

    private fun observeTemplateMutation() {
        viewModel.templateMutation.observe(viewLifecycleOwner, Observer {
            when (it) {
                TalkTemplateMutationResults.DeleteTemplate -> {
                    talkTemplateBottomSheetListener?.onSuccessUpdateTemplate(getString(R.string.template_list_success_delete_template))
                    this.dismiss()
                }
                TalkTemplateMutationResults.TemplateMutationSuccess -> {
                    talkTemplateBottomSheetListener?.onSuccessUpdateTemplate(getString(R.string.template_list_success_add_template))
                    this.dismiss()
                }
                is TalkTemplateMutationResults.DeleteTemplateFailed -> {
                    logException(it.throwable)
                    showToaster(getString(R.string.template_delete_toaster_fail))
                }
                is TalkTemplateMutationResults.MutationFailed -> {
                    logException(it.throwable)
                    showToaster(getString(R.string.template_list_toaster_fail))
                }
            }
        })
    }

    private fun showToaster(successMessage: String) {
        parentFragment?.view?.let {
            this.toaster = Toaster.build(it, successMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_ok))
            toaster?.show()
        }
    }

    private fun initButton(view: View) {
        talkEditTemplateSaveButton = view.findViewById(R.id.talkEditTemplateSaveButton)
        talkEditTemplateSaveButton?.setOnClickListener {
            if(!isEditMode) {
                TalkTemplateTracking.eventClickAddTemplateInBottomSheet(getShopId(), getUserId())
            }
            saveTemplate()
        }
    }

    private fun saveTemplate() {
        if (isEditMode) {
            editTemplate()
            return
        }
        addTemplate()
    }

    private fun getDataFromCacheManager() {
        val saveInstanceCacheManager = context?.let { SaveInstanceCacheManager(it, cacheManagerId) }
        saveInstanceCacheManager?.run {
            val dataWrapper = get<TalkTemplateDataWrapper>(KEY_TEMPLATE, TalkTemplateDataWrapper::class.java)
            dataWrapper?.let {
                isSeller = it.isSeller
                isEditMode = it.isEditMode
                if (isEditMode) {
                    index = it.index ?: -1
                    templateText = it.template ?: ""
                    allowDelete = it.allowDelete
                }
            }
        }
    }

    private fun addTemplate() {
        viewModel.addTemplate(isSeller, talkEditTemplateEditText?.text.toString())
    }

    private fun deleteTemplate() {
        viewModel.deleteSpecificTemplate(index, isSeller)
    }

    private fun editTemplate() {
        viewModel.updateSpecificTemplate(isSeller, talkEditTemplateEditText?.text.toString(), index)
    }

    private fun setupEditText(view: View) {
        talkEditTemplateEditText = view.findViewById(R.id.talkEditTemplateEditText)
        talkEditTemplateEditText?.apply {
            if (isEditMode) {
                post {
                    setText(templateText)
                }
            } else {
                post {
                    setHint(R.string.template_list_add_template_placeholder)
                    setText("")
                }
            }
            post {
                requestFocus()
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        if(s?.length == TEXT_LIMIT) {
                            showToaster(getString(R.string.template_list_add_edit_template_limit))
                        }
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
    }

    private fun getUserId(): String {
        return (activity as? UserSessionListener)?.getUserId() ?: ""
    }

    private fun getShopId(): String {
        return (activity as? UserSessionListener)?.getShopId() ?: ""
    }

    private fun logException(throwable: Throwable) {
        FirebaseLogger.logError(throwable)
    }
}
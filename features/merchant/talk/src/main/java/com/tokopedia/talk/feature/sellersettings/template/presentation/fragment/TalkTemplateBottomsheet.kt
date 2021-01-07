package com.tokopedia.talk.feature.sellersettings.template.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.talk.R
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController.setNavigationResult
import com.tokopedia.talk.feature.sellersettings.common.util.TalkSellerSettingsConstants
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateDataWrapper
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateMutationResults
import com.tokopedia.talk.feature.sellersettings.template.di.DaggerTalkTemplateComponent
import com.tokopedia.talk.feature.sellersettings.template.di.TalkTemplateComponent
import com.tokopedia.talk.feature.sellersettings.template.presentation.listener.TalkTemplateBottomSheetListener
import com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel.TalkEditTemplateViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_talk_edit_template.*
import javax.inject.Inject

class TalkTemplateBottomsheet : BottomSheetUnify(), HasComponent<TalkTemplateComponent> {

    companion object {
        const val REQUEST_KEY = "talk_template_request"
        const val KEY_TEMPLATE = "template"
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
    private var text: String = ""
    private var toaster: Snackbar? = null
    private var isEditMode: Boolean = false
    private var allowDelete: Boolean = false
    private var cacheManagerId = ""
    private var talkTemplateBottomSheetListener: TalkTemplateBottomSheetListener? = null

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
        setupEditText()
        observeTemplateMutation()
        initButton()
        if (isEditMode && allowDelete) {
            context?.let {
                setAction(getString(R.string.template_list_delete)) {
                    deleteTemplate()
                }
            }
        }
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
                TalkTemplateMutationResults.DeleteTemplateFailed -> {
                    showToaster(getString(R.string.template_delete_toaster_fail))
                }
                TalkTemplateMutationResults.MutationFailed -> {
                    showToaster(getString(R.string.template_list_toaster_fail))
                }
            }
        })
    }

    private fun showToaster(successMessage: String) {
        view?.let {
            this.toaster = Toaster.build(it, successMessage, Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.talk_ok))
            toaster?.let { toaster ->
                if (toaster.isShownOrQueued) {
                    return
                }
                toaster.show()
            }
        }
    }

    private fun initButton() {
        talkEditTemplateSaveButton.setOnClickListener {
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
                    text = it.template ?: ""
                    allowDelete = it.allowDelete
                }
            }
        }
    }

    private fun addTemplate() {
        viewModel.addTemplate(isSeller, talkEditTemplateEditText.text.toString())
    }

    private fun deleteTemplate() {
        viewModel.deleteSpecificTemplate(index, isSeller)
    }

    private fun editTemplate() {
        viewModel.updateSpecificTemplate(isSeller, talkEditTemplateEditText.text.toString(), index)
    }

    private fun setupEditText() {
        if (isEditMode) {
            talkEditTemplateEditText.setText(text)
        }
    }
}
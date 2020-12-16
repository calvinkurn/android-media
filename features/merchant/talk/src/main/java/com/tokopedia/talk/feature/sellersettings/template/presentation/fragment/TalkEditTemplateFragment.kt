package com.tokopedia.talk.feature.sellersettings.template.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.talk.R
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController.setNavigationResult
import com.tokopedia.talk.feature.sellersettings.common.util.TalkSellerSettingsConstants
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateDataWrapper
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateMutationResults
import com.tokopedia.talk.feature.sellersettings.template.di.DaggerTalkTemplateComponent
import com.tokopedia.talk.feature.sellersettings.template.di.TalkTemplateComponent
import com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel.TalkEditTemplateViewModel
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_talk_edit_template.*
import javax.inject.Inject

class TalkEditTemplateFragment : BaseDaggerFragment(), HasComponent<TalkTemplateComponent> {

    companion object {
        const val REQUEST_KEY = "talk_template_request"
        const val KEY_TEMPLATE = "template"
    }

    @Inject
    lateinit var viewModel: TalkEditTemplateViewModel

    private var isSeller: Boolean = false
    private var index: Int = -1
    private var text: String = ""
    private var toaster: Snackbar? = null
    private var isEditMode: Boolean = false
    private var toolbar: HeaderUnify? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getDataFromCacheManager()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_edit_template, container, false)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
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
        setupOnBackPressed()
        setupToolbar()
        setupEditText()
        observeTemplateMutation()
        initButton()
    }

    private fun observeTemplateMutation() {
        viewModel.templateMutation.observe(viewLifecycleOwner, Observer {
            when (it) {
                TalkTemplateMutationResults.DeleteTemplate -> {
                    setFragmentResultWithBundle(TalkSellerSettingsConstants.VALUE_DELETE)
                }
                TalkTemplateMutationResults.TemplateMutationSuccess -> {
                    setFragmentResultWithBundle(TalkSellerSettingsConstants.VALUE_ADD_EDIT)
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

    private fun setupToolbar() {
        val toolbar = activity?.findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)
        toolbar?.apply {
            clearToolbar()
            if (isEditMode) {
                setTitle(R.string.title_edit_template_page)
                addRightIcon(0).apply {
                    clearImage()
                    setImageDrawable(com.tokopedia.iconunify.getIconUnifyDrawable(context, IconUnify.DELETE, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700)))
                    setOnClickListener {
                        deleteTemplate()
                    }
                }
                return
            }
            setTitle(R.string.title_add_template_page)
        }
    }

    private fun setFragmentResultWithBundle(requestValue: String) {
        arguments?.let {
            val bundle = Bundle().apply {
                putString(TalkSellerSettingsConstants.KEY_ACTION, requestValue)
            }
            setNavigationResult(bundle, REQUEST_KEY)
            findNavController().navigateUp()
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
        arguments?.let {
            val cacheManagerId = TalkEditTemplateFragmentArgs.fromBundle(it).cacheManagerId
            val saveInstanceCacheManager = context?.let { SaveInstanceCacheManager(it, cacheManagerId) }

            saveInstanceCacheManager?.run {
                val dataWrapper = get<TalkTemplateDataWrapper>(KEY_TEMPLATE, TalkTemplateDataWrapper::class.java)
                dataWrapper?.let {
                    isSeller = it.isSeller
                    isEditMode = it.isEditMode
                    if(isEditMode) {
                        index = it.index ?: -1
                        text = it.template ?: ""
                    }
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
        if(isEditMode) {
            talkEditTemplateEditText.setText(text)
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                clearToolbar()
                findNavController().navigateUp()
            }
        })
    }

    private fun clearToolbar() {
        toolbar?.rightContentView?.removeAllViews()
    }
}
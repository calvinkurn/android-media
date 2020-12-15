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
import com.tokopedia.header.HeaderUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.clearImage
import com.tokopedia.talk.R
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController.setNavigationResult
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
        const val KEY_ACTION = "action"
        const val VALUE_EDIT = "edit"
        const val VALUE_ADD = "add"
        const val VALUE_DELETE = "delete"
    }

    @Inject
    lateinit var viewModel: TalkEditTemplateViewModel

    private var index: Int = 0
    private var text: String = ""
    private var toaster: Snackbar? = null

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
        observeTemplateMutation()
        initButton()
    }

    private fun observeTemplateMutation() {
        viewModel.templateMutation.observe(viewLifecycleOwner, Observer {
            when (it) {
                TalkTemplateMutationResults.AddTemplate -> {
                    showToaster(getString(R.string.template_list_success_add_template), false)
                }
                TalkTemplateMutationResults.ToggleTemplate -> {
                    showToaster(getString(R.string.template_list_success_add_template), false)
                }
                TalkTemplateMutationResults.ArrangeTemplate -> {
                    showToaster(getString(R.string.template_list_success_add_template), false)
                }
                TalkTemplateMutationResults.DeleteTemplate -> {
                    showToaster(getString(R.string.template_list_success_delete_template), false)
                }
                TalkTemplateMutationResults.UpdateTemplate -> {
                    showToaster(getString(R.string.template_list_success_add_template), false)
                }
                TalkTemplateMutationResults.MutationFailed -> {
                    showToaster(getString(R.string.template_list_toaster_fail), true)
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

    private fun setupToolbar() {
        val toolbar = activity?.findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)
        toolbar?.apply {
            if (isEditMode()) {
                rightContentView.removeAllViews()
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
                putString(KEY_ACTION, requestValue)
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
        if (isEditMode()) {
            editTemplate()
            return
        }
        addTemplate()
    }

    private fun isEditMode(): Boolean {
        return false
//        return arguments?.let { TalkEditTemplateFragmentArgs.fromBundle(it).isEdit } ?: false
    }

    private fun addTemplate() {
//        viewModel.addTemplate()
    }

    private fun deleteTemplate() {
//        viewModel.deleteSpecificTemplate()
    }

    private fun editTemplate() {
//        viewModel.updateSpecificTemplate()
    }


    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

}
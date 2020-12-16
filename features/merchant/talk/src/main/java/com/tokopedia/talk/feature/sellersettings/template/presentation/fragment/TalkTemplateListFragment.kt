package com.tokopedia.talk.feature.sellersettings.template.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.sellersettings.template.di.DaggerTalkTemplateComponent
import com.tokopedia.talk.feature.sellersettings.template.di.TalkTemplateComponent
import com.tokopedia.talk.R
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController.getNavigationResult
import com.tokopedia.talk.feature.sellersettings.common.navigation.NavigationController.removeNavigationResult
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateDataWrapper
import com.tokopedia.talk.feature.sellersettings.template.data.TalkTemplateMutationResults
import com.tokopedia.talk.feature.sellersettings.template.presentation.adapter.TalkTemplateListAdapter
import com.tokopedia.talk.feature.sellersettings.template.presentation.adapter.TalkTemplateListItemTouchHelperCallback
import com.tokopedia.talk.feature.sellersettings.template.presentation.listener.TalkTemplateListListener
import com.tokopedia.talk.feature.sellersettings.template.presentation.viewmodel.TalkTemplateViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_talk_template_list.*
import javax.inject.Inject

class TalkTemplateListFragment : BaseDaggerFragment(), HasComponent<TalkTemplateComponent>, TalkTemplateListListener {

    companion object {
        const val MAX_TEMPLATE_SIZE = 5
    }

    @Inject
    lateinit var viewModel: TalkTemplateViewModel

    private var isSeller: Boolean = false
    private val adapter = TalkTemplateListAdapter(this)
    private var toaster: Snackbar? = null
    private var toolbar: HeaderUnify? = null

    override fun getScreenName(): String {
        return ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isSeller = TalkTemplateListFragmentArgs.fromBundle(it).isSeller
        }
        getTemplateList()
    }

    override fun initInjector() {
        component?.inject(this)
    }

    override fun onEditClicked(template: String, index: Int) {
        goToEdit(template, index)
    }

    override fun onItemMove(originalIndex: Int, moveTo: Int) {
        viewModel.arrangeTemplate(originalIndex, moveTo, isSeller)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_talk_template_list, container, false)
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
        initRecyclerView()
        observeTemplateList()
        observeTemplateMutation()
        setupOnBackPressed()
        setupAddTemplateButton()
        setToolbarTitle()
        onFragmentResult()
    }

    private fun getTemplateList() {
        viewModel.getTemplateList(isSeller)
    }

    private fun observeTemplateMutation() {
        viewModel.templateMutation.observe(viewLifecycleOwner, Observer {
            when (it) {
                TalkTemplateMutationResults.TemplateMutationSuccess -> {
                    showToaster(getString(R.string.template_list_success_add_template), false)
                }
                TalkTemplateMutationResults.RearrangeTemplateFailed -> {
                    showToaster(getString(R.string.template_list_rearrange_toaster_fail), true)
                }
                else -> {
                    showToaster(getString(R.string.template_list_toaster_fail), true)
                }
            }
        })
    }

    private fun observeTemplateList() {
        viewModel.templateList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (isSeller) {
                        renderList(it.data.sellerTemplate.templates)
                        return@Observer
                    }
                    renderList(it.data.buyerTemplate.templates)
                }
                is Fail -> {

                }
            }
        })
    }

    private fun renderList(templates: List<String>) {
        talkTemplateListAddButton.isEnabled = templates.shouldEnableButton()
        adapter.setData(templates)
        talkTemplateListRecyclerView.show()
        initSwitch()
    }

    private fun initRecyclerView() {
        val touchHelperCallback = TalkTemplateListItemTouchHelperCallback(adapter)
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        talkTemplateListRecyclerView.apply {
            adapter = this@TalkTemplateListFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
        itemTouchHelper.attachToRecyclerView(talkTemplateListRecyclerView)
    }

    private fun initSwitch() {
        talkTemplateListSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.enableTemplate(isChecked)
            if (isChecked) {
                talkTemplateListRecyclerView.show()
                return@setOnCheckedChangeListener
            }
            talkTemplateListRecyclerView.hide()
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        })
    }

    private fun setToolbarTitle() {
        toolbar = activity?.findViewById<HeaderUnify>(R.id.talk_seller_settings_toolbar)
        toolbar?.setTitle(R.string.title_template_page)
    }

    private fun setupAddTemplateButton() {
        talkTemplateListAddButton.setOnClickListener {
            goToAdd()
        }
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

    private fun List<String>.shouldEnableButton(): Boolean {
        return this.size < MAX_TEMPLATE_SIZE
    }

    private fun goToEdit(template: String, index: Int) {
        val destination = TalkTemplateListFragmentDirections.actionTalkTemplateListFragmentToTalkTemplateEditFragment()
        destination.cacheManagerId = putDataIntoCacheManager(true, template, index)
        NavigationController.navigate(this, destination)
    }

    private fun goToAdd() {
        val destination = TalkTemplateListFragmentDirections.actionTalkTemplateListFragmentToTalkTemplateEditFragment()
        destination.cacheManagerId = putDataIntoCacheManager(false, null, null)
        NavigationController.navigate(this, destination)
    }

    private fun onFragmentResult() {
        getNavigationResult(TalkEditTemplateFragment.REQUEST_KEY)?.observe(viewLifecycleOwner, Observer {
            it?.let {
                toolbar?.rightContentView?.removeAllViews()
                when(it.getString(TalkEditTemplateFragment.KEY_ACTION, "")) {
                    TalkEditTemplateFragment.VALUE_DELETE -> {
                        showToaster(getString(R.string.template_list_success_delete_template), false)
                    }
                    TalkEditTemplateFragment.VALUE_ADD_EDIT -> {
                        showToaster(getString(R.string.template_list_success_add_template), false)
                    }
                }
            }
            removeNavigationResult(TalkEditTemplateFragment.REQUEST_KEY)
            getTemplateList()
        })
    }

    private fun putDataIntoCacheManager(isEditMode: Boolean, template: String?, index: Int?): String {
        val cacheManager = context?.let { SaveInstanceCacheManager(it, true) }
        cacheManager?.apply {
            put(TalkEditTemplateFragment.KEY_TEMPLATE, TalkTemplateDataWrapper(isSeller, isEditMode, template, index))
        }
        return cacheManager?.id ?: ""
    }

}
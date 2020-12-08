package com.tokopedia.talk.feature.sellersettings.template.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.TalkInstance
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.talk.feature.sellersettings.template.di.DaggerTalkTemplateComponent
import com.tokopedia.talk.feature.sellersettings.template.di.TalkTemplateComponent
import com.tokopedia.talk.R
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

    @Inject
    lateinit var viewModel: TalkTemplateViewModel

    private var isSeller: Boolean = false
    private val adapter = TalkTemplateListAdapter(this)

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

    override fun onEditClicked(template: String) {
        view?.let { Toaster.build(it, "Edit Clicked").show() }
    }

    override fun onItemMove(originalIndex: Int, moveTo: Int) {
        viewModel.arrangeTemplate(originalIndex, moveTo, isSeller)
    }

    override fun onItemRemoved(index: Int) {
        viewModel.deleteSpecificTemplate(index, isSeller)
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
    }

    private fun getTemplateList() {
        viewModel.getTemplateList(isSeller)
    }

    private fun observeTemplateList() {
        viewModel.templateList.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    if(isSeller) {
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
            viewModel.enableTemplateUseCase(isChecked)
            if(isChecked) {
                talkTemplateListRecyclerView.show()
                return@setOnCheckedChangeListener
            }
            talkTemplateListRecyclerView.hide()
        }
    }
}
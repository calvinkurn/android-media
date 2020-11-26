package com.tokopedia.product.addedit.draft.presentation.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.draft.di.AddEditProductDraftComponent
import com.tokopedia.product.addedit.draft.mapper.AddEditProductMapper
import com.tokopedia.product.addedit.draft.presentation.adapter.ProductDraftListAdapter
import com.tokopedia.product.addedit.draft.presentation.listener.ProductDraftListListener
import com.tokopedia.product.addedit.draft.presentation.model.ProductDraftUiModel
import com.tokopedia.product.addedit.draft.presentation.viewmodel.AddEditProductDraftViewModel
import com.tokopedia.product.addedit.preview.presentation.activity.AddEditProductPreviewActivity
import com.tokopedia.product.manage.common.draft.data.model.ProductDraft
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_add_edit_product_draft.*
import javax.inject.Inject

class AddEditProductDraftFragment : BaseDaggerFragment(), ProductDraftListListener {

    companion object {
        const val SCREEN_NAME = "/draft product page"

        @JvmStatic
        fun newInstance() = AddEditProductDraftFragment()
    }

    @Inject
    lateinit var viewModel: AddEditProductDraftViewModel

    private var draftListAdapter: ProductDraftListAdapter? = null
    private var drafts = mutableListOf<ProductDraftUiModel>()
    private var deletePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbarActions()
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        observer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_draft, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_product_draft, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_label_add_product -> {
                val intent = AddEditProductPreviewActivity.createInstance(context)
                startActivity(intent)
            }
            R.id.item_delete_all_draft -> {
                viewModel.deleteAllProductDraft()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(AddEditProductDraftComponent::class.java).inject(this)
    }

    override fun onDraftClickListener(draftId: Long) {
        val intent = AddEditProductPreviewActivity.createInstance(context, draftId.toString())
        startActivity(intent)
    }

    override fun onDraftDeleteListener(draftId: Long, position: Int) {
        viewModel.deleteProductDraft(draftId)
        deletePosition = position
    }

    private fun setupToolbarActions() {
        activity?.findViewById<HeaderUnify>(R.id.toolbar_draft)?.apply {
            headerTitle = getString(R.string.label_title_draft_product)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    private fun setup() {
        draftListAdapter = ProductDraftListAdapter(this, drafts)
        rvDraft.adapter = draftListAdapter
        rvDraft.layoutManager = LinearLayoutManager(context)

        viewModel.getAllProductDraft()
    }

    private fun observer() {
        observe(viewModel.drafts) {
            when(it) {
                is Success -> draftListAdapter?.setDrafts(it.data)
                is Fail -> {}
            }
        }

        observe(viewModel.deleteDraft) {
            when(it) {
                is Success -> draftListAdapter?.deleteDraft(deletePosition)
                is Fail -> {}
            }
        }

        observe(viewModel.deleteAllDraft) {
            when(it) {
                is Success -> draftListAdapter?.deleteAllDrafts()
                is Fail -> {}
            }
        }
    }
}
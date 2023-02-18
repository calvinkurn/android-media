package com.tokopedia.product.addedit.draft.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.common.TabletAdaptiveActivity
import com.tokopedia.product.addedit.databinding.ActivityAddEditProductDraftBinding
import com.tokopedia.product.addedit.draft.di.AddEditProductDraftComponent
import com.tokopedia.product.addedit.draft.di.AddEditProductDraftModule
import com.tokopedia.product.addedit.draft.di.DaggerAddEditProductDraftComponent
import com.tokopedia.product.addedit.draft.presentation.fragment.AddEditProductDraftFragment

open class AddEditProductDraftActivity :
    TabletAdaptiveActivity(),
    HasComponent<AddEditProductDraftComponent> {

    private val addEditProductDraftFragment: AddEditProductDraftFragment by lazy {
        AddEditProductDraftFragment.newInstance()
    }

    override fun getLayoutRes() = R.layout.activity_add_edit_product_draft

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getNewFragment(): Fragment? = addEditProductDraftFragment

    private var binding: ActivityAddEditProductDraftBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditProductDraftBinding.inflate(layoutInflater)
        val view = binding?.root
        view?.run { setContentView(this) }
        binding?.run { setupView(this) }
    }

    private fun setupView(binding: ActivityAddEditProductDraftBinding) {
        val toolbarDraft = binding.toolbarDraft
        toolbarDraft.headerTitle = getString(R.string.label_title_draft_product)
        setSupportActionBar(toolbarDraft)
    }

    override fun getComponent(): AddEditProductDraftComponent {
        return DaggerAddEditProductDraftComponent
            .builder()
            .addEditProductComponent(AddEditProductComponentBuilder.getComponent(application))
            .addEditProductDraftModule(AddEditProductDraftModule())
            .build()
    }
}

package com.tokopedia.product.addedit.draft.presentation.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.draft.di.AddEditProductDraftComponent
import com.tokopedia.product.addedit.draft.di.AddEditProductDraftModule
import com.tokopedia.product.addedit.draft.di.DaggerAddEditProductDraftComponent
import com.tokopedia.product.addedit.draft.presentation.fragment.AddEditProductDraftFragment


open class AddEditProductDraftActivity : BaseSimpleActivity(), HasComponent<AddEditProductDraftComponent> {

    private var toolbarDraft: HeaderUnify? = null
    private val addEditProductDraftFragment: AddEditProductDraftFragment by lazy {
        AddEditProductDraftFragment.newInstance()
    }

    override fun getLayoutRes() = R.layout.activity_add_edit_product_draft

    override fun getParentViewResourceID(): Int = R.id.parent_view

    override fun getNewFragment(): Fragment? = addEditProductDraftFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_product_draft)
        toolbarDraft = findViewById(R.id.toolbar_draft)
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
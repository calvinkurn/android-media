package com.tokopedia.product.addedit.stub

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.draft.presentation.activity.AddEditProductDraftActivity
import com.tokopedia.product.addedit.draft.presentation.fragment.AddEditProductDraftFragment

class AddEditProductDraftActivityStub : AddEditProductDraftActivity() {

    private val fragmentStub: AddEditProductDraftFragmentStub by lazy {
        AddEditProductDraftFragmentStub()
    }

    override fun getNewFragment() = fragmentStub
}

class AddEditProductDraftFragmentStub : AddEditProductDraftFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val geDraft: View? = view.findViewById(R.id.geDraft)
        val emptyLayout: View? = view.findViewById(R.id.emptyLayout)
        observe(viewModel.drafts) {
            geDraft?.hide()
            emptyLayout?.show()
        }
    }
}

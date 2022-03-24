package com.tokopedia.tokofood.example

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.tokopedia.abstraction.base.view.activity.BaseMultiFragActivity
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment

class ExampleTokofoodActivity : BaseMultiFragActivity(), HasViewModel<MultipleFragmentsViewModel> {

    val viewModel: MultipleFragmentsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onRestoreSavednstanceState()
    }

    override fun getRootFragment(): BaseMultiFragment {
        return FragmentA()
    }

    override fun mapUriToFragment(uri: Uri): BaseMultiFragment? {
        // tokopedia://tokofood
        if (uri.host == "tokofood") {
            var f: BaseMultiFragment? = null
            if (uri.path == "/home") { // tokopedia://tokofood/home
                f = FragmentA()
            } else if (uri.path == "/b") { // tokopedia://tokofood/b
                f = FragmentB()
            }
            if (f != null) {
                f.arguments = Bundle().apply {
                    putString(Constant.DATA_KEY, uri.toString())
                }
                return f
            }
        }
        return null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.onSavedInstanceState()
    }

    override fun viewModel(): MultipleFragmentsViewModel = viewModel
}
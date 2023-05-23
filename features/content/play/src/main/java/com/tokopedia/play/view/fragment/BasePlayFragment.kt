package com.tokopedia.play.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.view.viewmodel.PlayViewModel

/**
 * @author by astidhiyaa on 22/05/23
 */
open class BasePlayFragment : Fragment() { //Dialog?

    protected lateinit var viewModel: PlayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        //TODO(): wrap with try catch to check use when to check each
        val parentFragment = requireParentFragment()
        val grandParent = parentFragment.requireParentFragment()
        val child = grandParent.requireParentFragment()
        viewModel = ViewModelProvider(
            child, (child as PlayFragment).viewModelProviderFactory
        )[PlayViewModel::class.java]
        super.onCreate(savedInstanceState)
    }
}

package com.tokopedia.play.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.play.R
import com.tokopedia.play.view.viewmodel.PlayViewModel
import java.net.UnknownHostException

/**
 * @author by astidhiyaa on 22/05/23
 */
open class BasePlayFragment : Fragment() {

    protected lateinit var viewModel: PlayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val parentFragment = requireParentFragment()
        val grandParent = parentFragment.requireParentFragment()
        val child = grandParent.requireParentFragment()
        viewModel = ViewModelProvider(
            child, (child as PlayFragment).viewModelProviderFactory
        )[PlayViewModel::class.java]
        super.onCreate(savedInstanceState)
    }

    fun generateErrorMessage(throwable: Throwable): String {
        return if (throwable is UnknownHostException) {
            getString(R.string.play_explore_widget_noconn_errmessage)
        } else {
            getString(
                R.string.play_explore_widget_default_errmessage
            )
        }
    }
}

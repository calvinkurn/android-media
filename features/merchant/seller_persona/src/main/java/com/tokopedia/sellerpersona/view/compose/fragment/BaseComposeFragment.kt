package com.tokopedia.sellerpersona.view.compose.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.sellerpersona.R
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by @ilhamsuaib on 13/10/23.
 */

abstract class BaseComposeFragment : Fragment() {

    private var isErrorToasterVisible = false
    private val toasterCallback by lazy {
        object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                isErrorToasterVisible = false
            }
        }
    }
    private val backPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                setOnBackPressed()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnBackPressed()
    }

    protected open fun setOnBackPressed() {
        view?.findNavController()?.navigateUp()
    }

    protected fun setComposeViewContent(context: Context, content: @Composable () -> Unit): View {
        return ComposeView(context).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent(content)
        }
    }

    protected open fun showGeneralErrorToaster() {
        if (isErrorToasterVisible) return
        isErrorToasterVisible = true
        view?.run {
            val dp64 = context.resources.getDimensionPixelSize(
                unifyprinciplesR.dimen.layout_lvl7
            )
            Toaster.toasterCustomBottomHeight = dp64
            val toaster = Toaster.build(
                rootView,
                context.getString(R.string.sp_toaster_error_message),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR,
                context.getString(R.string.sp_oke)
            )
            toaster.removeCallback(toasterCallback)
            toaster.addCallback(toasterCallback)
            toaster.show()
        }
    }

    private fun setupOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner, backPressedCallback
        )
    }
}
package com.tokopedia.stories.creation.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created By : Jonathan Darwin on September 20, 2023
 */
class StoriesCreationErrorBottomSheet : BottomSheetUnify() {

    private var throwable: Throwable = Throwable()

    var listener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        clearContentPadding = true
        overlayClickDismiss = false
        isSkipCollapseState = true
        isCancelable = false
        bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_EXPANDED

        setCloseClickListener {
            listener?.onClose()
        }

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                AndroidView(
                    modifier = Modifier.padding(bottom = 16.dp),
                    factory = { context ->
                        GlobalError(context).apply {
                            setType(
                                when (throwable) {
                                    is ConnectException,
                                    is UnknownHostException,
                                    is SocketTimeoutException -> GlobalError.NO_CONNECTION
                                    else -> GlobalError.SERVER_ERROR
                                }
                            )
                            setActionClickListener {
                                listener?.onRetry()
                            }
                        }
                    }
                )
            }
        }

        setChild(composeView)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager, throwable: Throwable) {
        this.throwable = throwable

        if (!isAdded) show(fragmentManager, TAG)
    }

    interface Listener {
        fun onRetry()

        fun onClose()
    }

    companion object {
        private const val TAG = "StoriesCreationErrorBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): StoriesCreationErrorBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? StoriesCreationErrorBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesCreationErrorBottomSheet::class.java.name
            ) as StoriesCreationErrorBottomSheet
        }
    }
}

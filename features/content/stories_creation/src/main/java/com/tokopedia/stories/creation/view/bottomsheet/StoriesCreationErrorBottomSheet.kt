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
import com.tokopedia.stories.creation.view.model.exception.NotEligibleException
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created By : Jonathan Darwin on September 20, 2023
 */
class StoriesCreationErrorBottomSheet : BottomSheetUnify() {

    var listener: Listener? = null

    private val errorType: Int
        get() = arguments?.getInt(KEY_ERROR_TYPE) ?: GlobalError.SERVER_ERROR

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
                            setType(errorType)

                            setActionClickListener {
                                listener?.onRetry(errorType)
                            }
                        }
                    }
                )
            }
        }

        setChild(composeView)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    interface Listener {
        fun onRetry(errorType: Int)

        fun onClose()
    }

    companion object {
        private const val TAG = "StoriesCreationErrorBottomSheet"

        private const val KEY_ERROR_TYPE = "KEY_ERROR_TYPE"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            throwable: Throwable
        ): StoriesCreationErrorBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? StoriesCreationErrorBottomSheet
            val instance = oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                StoriesCreationErrorBottomSheet::class.java.name
            ) as StoriesCreationErrorBottomSheet

            return instance.apply {
                arguments = Bundle().apply {
                    putInt(KEY_ERROR_TYPE, getErrorType(throwable))
                }
            }
        }

        private fun getErrorType(throwable: Throwable): Int {
            return when (throwable) {
                is ConnectException,
                is UnknownHostException,
                is SocketTimeoutException -> GlobalError.NO_CONNECTION
                is NotEligibleException -> GlobalError.PAGE_NOT_FOUND
                else -> GlobalError.SERVER_ERROR
            }
        }
    }
}

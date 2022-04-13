package com.tokopedia.play_common.view.game.giveaway

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.tokopedia.kotlin.extensions.view.getScreenWidth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Created by kenny.hadisaputra on 12/04/22
 */
class GiveawayWidgetDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return GiveawayWidgetView(inflater.context)
    }

    override fun onResume() {
        super.onResume()

        val window = dialog?.window ?: return
        window.setLayout(
            (WIDTH_PERCENTAGE * getScreenWidth()).toInt(),
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isVisible) show(fragmentManager, TAG)
    }

    fun onView(updateFn: GiveawayWidgetView.() -> Unit) {
        lifecycleScope.launchWhenCreated {
            awaitView()

            val view = view as? GiveawayWidgetView ?: return@launchWhenCreated
            updateFn(view)
        }
    }

    private suspend fun awaitView() = suspendCancellableCoroutine<LifecycleOwner> { cont ->
        if (view != null && cont.isActive) {
            cont.resume(viewLifecycleOwner)
        } else {
            val observer = object : Observer<LifecycleOwner> {
                override fun onChanged(owner: LifecycleOwner?) {
                    if (owner != null && cont.isActive) cont.resume(owner)
                    viewLifecycleOwnerLiveData.removeObserver(this)
                }
            }

            cont.invokeOnCancellation {
                viewLifecycleOwnerLiveData.removeObserver(observer)
            }

            viewLifecycleOwnerLiveData.observe(this, observer)
        }
    }

    companion object {
        private const val TAG = "GiveawayWidgetDialogFragment"

        private const val WIDTH_PERCENTAGE = 0.6

        fun getOrCreate(fragmentManager: FragmentManager): GiveawayWidgetDialogFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? GiveawayWidgetDialogFragment
            return oldInstance ?: GiveawayWidgetDialogFragment()
        }
    }
}
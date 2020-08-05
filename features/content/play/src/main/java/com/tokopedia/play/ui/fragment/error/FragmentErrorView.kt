package com.tokopedia.play.ui.fragment.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.fragment.PlayErrorFragment
import com.tokopedia.play.view.fragment.PlayUserInteractionFragment

/**
 * Created by jegul on 05/05/20
 */
class FragmentErrorView(
        private val channelId: String,
        container: ViewGroup,
        private val fragmentManager: FragmentManager
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_fragment_error, container, true)
                    .findViewById(R.id.fl_global_error)

    init {
        fragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG) ?: getPlayErrorFragment().also {
            fragmentManager.beginTransaction()
                    .replace(view.id, it, ERROR_FRAGMENT_TAG)
                    .commit()
        }
    }

    override val containerId: Int = view.id

    override fun show() {
        view.show()
    }

    override fun hide() {
        view.hide()
    }

    private fun getPlayErrorFragment(): Fragment {
        val fragmentFactory = fragmentManager.fragmentFactory
        return fragmentFactory.instantiate(container.context.classLoader, PlayErrorFragment::class.java.name).apply {
            arguments = Bundle().apply {
                putString(PLAY_KEY_CHANNEL_ID, channelId)
            }
        }
    }

    companion object {
        private const val ERROR_FRAGMENT_TAG = "fragment_error"
    }
}
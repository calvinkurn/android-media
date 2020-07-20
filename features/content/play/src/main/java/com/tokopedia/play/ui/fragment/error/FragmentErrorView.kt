package com.tokopedia.play.ui.fragment.error

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.R
import com.tokopedia.play.component.UIView
import com.tokopedia.play.view.fragment.PlayErrorFragment

/**
 * Created by jegul on 05/05/20
 */
class FragmentErrorView(
        channelId: String,
        container: ViewGroup,
        fragmentManager: FragmentManager
) : UIView(container) {

    private val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.view_fragment_error, container, true)
                    .findViewById(R.id.fl_global_error)

    init {
        fragmentManager.findFragmentByTag(ERROR_FRAGMENT_TAG) ?: PlayErrorFragment.newInstance(channelId).also {
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

    companion object {
        private const val ERROR_FRAGMENT_TAG = "fragment_error"
    }
}
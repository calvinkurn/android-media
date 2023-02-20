package com.tokopedia.play.view.viewcomponent

import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import com.tokopedia.play.R
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.Toaster

/**
 * Created by jegul on 03/08/20
 */
class EndLiveInfoViewComponent(
    container: ViewGroup,
    @IdRes idRes: Int
) : ViewComponent(container, idRes) {

    private val txtLiveEndedTitle = findViewById<TextView>(R.id.txt_live_ended_title)

    fun setInfo(title: String) {
        txtLiveEndedTitle.text = title
    }

    fun showToaster(text: String) {
        Toaster.build(
            view = rootView,
            text = text,
            type = Toaster.TYPE_ERROR,
        ).show()
    }
}

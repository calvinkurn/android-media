package com.tokopedia.play.widget.ui

import com.tokopedia.play.widget.ui.listener.PlayWidgetInternalListener

/**
 * Created by jegul on 22/10/20
 */
internal interface IPlayWidgetView {

    fun setWidgetInternalListener(listener: PlayWidgetInternalListener?)
}
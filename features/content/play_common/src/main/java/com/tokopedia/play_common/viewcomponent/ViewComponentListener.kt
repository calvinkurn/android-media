package com.tokopedia.play_common.viewcomponent

/**
 * Created by jegul on 21/09/20
 */
interface ViewComponentListener<VC: IViewComponent> {

    fun onViewDestroyed(view: VC)
}
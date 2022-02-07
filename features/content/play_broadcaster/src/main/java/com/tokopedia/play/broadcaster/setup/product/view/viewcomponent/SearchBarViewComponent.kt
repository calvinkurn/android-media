package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play.broadcaster.view.custom.PlaySearchBar
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
class SearchBarViewComponent(
    private val view: PlaySearchBar,
    eventBus: EventBus<in Event>,
) : ViewComponent(view) {

    init {
        view.setListener(object : PlaySearchBar.Listener {
            override fun onNewKeyword(view: PlaySearchBar, keyword: String) {
                eventBus.emit(Event.OnSearched(keyword))
            }

            override fun onSearchButtonClicked(view: PlaySearchBar, keyword: String) {

            }

            override fun onCleared(view: PlaySearchBar) {
                eventBus.emit(Event.OnSearched(""))
            }
        })
    }

    fun setPlaceholder(placeholder: String) {
        view.setHint(placeholder)
    }

    sealed class Event {
        data class OnSearched(val keyword: String) : Event()
    }
}
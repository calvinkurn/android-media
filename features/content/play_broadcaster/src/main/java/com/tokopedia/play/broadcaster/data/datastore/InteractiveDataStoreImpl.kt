package com.tokopedia.play.broadcaster.data.datastore

import javax.inject.Inject


/**
 * Created by mzennis on 21/07/21.
 */
class InteractiveDataStoreImpl @Inject constructor() : InteractiveDataStore {

    private var interactiveId: String = ""
    private var mActiveInteractiveTitle = DEFAULT_INTERACTIVE_TITLE

    override fun getInteractiveId(): String {
        return interactiveId
    }

    override fun getActiveInteractiveTitle(): String {
        return mActiveInteractiveTitle
    }

    override fun setActiveInteractiveTitle(title: String) {
        this.mActiveInteractiveTitle = title
    }

    override fun setInteractiveId(id: String) {
        interactiveId = id
    }

    companion object {
        const val DEFAULT_INTERACTIVE_TITLE = "Giveaway "
    }
}
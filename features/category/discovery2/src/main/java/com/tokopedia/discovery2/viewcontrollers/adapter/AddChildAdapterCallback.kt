package com.tokopedia.discovery2.viewcontrollers.adapter

interface AddChildAdapterCallback {
    fun addChildAdapter(discoveryRecycleAdapter: DiscoveryRecycleAdapter)

    fun notifyMergeAdapter()
}
package com.tkpd.remoteresourcerequest.utils

interface DeferredCallback {
    fun logDeferred(message: String)
    fun onDownloadStateChanged(resUrl: String, isFailed: Boolean)
    fun onCacheHit(resUrl: String)
}


object CallbackDispatcher {
    fun dispatchLog(deferredCallback: DeferredCallback?, message: String) {
        deferredCallback?.logDeferred("DeferredCallback $message")
    }

    fun onDownloadState(deferredCallback: DeferredCallback?, resUrl: String, isFailed: Boolean) {
        deferredCallback?.onDownloadStateChanged(resUrl, isFailed)
    }

    fun onCacheHit(deferredCallback: DeferredCallback?, resUrl: String) {
        deferredCallback?.onCacheHit(resUrl)
    }
}
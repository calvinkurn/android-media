package com.tkpd.remoteresourcerequest.callback

/**
 * This callback is mainly for logging purpose. It can be initialized using ResourceDownloadManager
 * addDeferredCallback() method just before calling ResourceDownloadManager initialize() method.
 * [logDeferred] helps to log every state of a task associated with a url.
 * use [onDownloadStateChanged] to log download state (i.e. success/fail) of each url.
 * use [onCacheHit] when you need to know which url are present in db.
 *
 */
interface DeferredCallback {
    fun logDeferred(message: String)
    fun onDownloadStateChanged(resUrl: String, isFailed: Boolean)
    fun onCacheHit(resUrl: String)
}


object CallbackDispatcher {
    fun dispatchLog(deferredCallback: DeferredCallback?, message: String) {
        deferredCallback?.logDeferred(message)
    }

    fun onDownloadState(deferredCallback: DeferredCallback?, resUrl: String, isFailed: Boolean) {
        deferredCallback?.onDownloadStateChanged(resUrl, isFailed)
    }

    fun onCacheHit(deferredCallback: DeferredCallback?, resUrl: String) {
        deferredCallback?.onCacheHit(resUrl)
    }
}

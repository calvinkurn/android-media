package com.tkpd.remoteresourcerequest.callback

/**
 * Use this class as a callback for requesting download of any file. It will return the
 * downloaded file path and the requested url in onTaskCompleted() if download is successful .
 * It may be possible that the resource is not available at the requested url in which
 * case onTaskFailed() callback is returned.
 */
interface DeferredTaskCallback {
    fun onTaskCompleted(resourceUrl: String?, filePath: String?)
    fun onTaskFailed(resourceUrl: String?)
}

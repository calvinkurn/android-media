package com.tkpd.remoteresourcerequest.task

interface DeferredTaskCallback {
    fun onTaskCompleted(resourceUrl: String?)
}

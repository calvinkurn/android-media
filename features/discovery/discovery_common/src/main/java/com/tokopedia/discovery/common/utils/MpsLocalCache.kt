package com.tokopedia.discovery.common.utils

interface MpsLocalCache {
    fun isFirstMpsSuccess() : Boolean
    fun markFirstMpsSuccess()

    fun shouldAnimatePlusIcon() : Boolean
}

package com.tokopedia.profilecommon.domain.usecase

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

abstract class BaseUseCase<out OutputType> {
    abstract suspend fun getData(): OutputType
}

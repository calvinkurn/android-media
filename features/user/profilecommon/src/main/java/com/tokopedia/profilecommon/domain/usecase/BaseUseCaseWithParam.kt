package com.tokopedia.profilecommon.domain.usecase

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */


abstract class BaseUseCaseWithParam<in InputType, out OutputType> {
    abstract suspend fun getData(parameter: InputType): OutputType
}
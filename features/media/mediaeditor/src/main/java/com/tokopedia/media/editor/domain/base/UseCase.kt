package com.tokopedia.media.editor.domain.base

abstract class UseCase<P, R> {
    abstract fun execute(params: P): R

    operator fun invoke(params: P): R {
        return execute(params)
    }
}
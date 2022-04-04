package com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewMediaTypeFactory

sealed interface CreateReviewMediaUiModel : Visitable<CreateReviewMediaTypeFactory> {

    companion object {
        const val PAYLOAD_MEDIA_STATE = "payloadMediaState"
    }

    val uri: String
    val uploadId: String
    val state: State

    fun areItemsTheSame(other: Any?): Boolean
    fun areContentsTheSame(other: Any?): Boolean
    fun getChangePayload(other: Any?): Bundle

    data class Image(
        override val uri: String,
        override val uploadId: String = "",
        override val state: State,
    ) : CreateReviewMediaUiModel {
        override fun areItemsTheSame(other: Any?): Boolean {
            return other is Image && uri == other.uri
        }

        override fun areContentsTheSame(other: Any?): Boolean {
            return other is Image && state == other.state
        }

        override fun getChangePayload(other: Any?): Bundle {
            return Bundle().apply {
                if (other !is Image) return@apply
                if (state != other.state) {
                    putSerializable(PAYLOAD_MEDIA_STATE, other.state)
                }
            }
        }

        override fun type(typeFactory: CreateReviewMediaTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    data class Video(
        override val uri: String,
        override val uploadId: String = "",
        override val state: State
    ) : CreateReviewMediaUiModel {
        override fun areItemsTheSame(other: Any?): Boolean {
            return other is Video && uri == other.uri
        }

        override fun areContentsTheSame(other: Any?): Boolean {
            return other is Image && state == other.state
        }

        override fun getChangePayload(other: Any?): Bundle {
            return Bundle().apply {
                if (other !is Video) return@apply
                if (state != other.state) {
                    putSerializable(PAYLOAD_MEDIA_STATE, other.state)
                }
            }
        }

        override fun type(typeFactory: CreateReviewMediaTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    object AddSmall : CreateReviewMediaUiModel {
        override val uri: String
            get() = ""
        override val uploadId: String
            get() = ""
        override val state: State
            get() = State.UPLOADED

        override fun areItemsTheSame(other: Any?): Boolean {
            return other is AddSmall
        }

        override fun areContentsTheSame(other: Any?): Boolean {
            return other is AddSmall
        }

        override fun getChangePayload(other: Any?): Bundle {
            return Bundle()
        }

        override fun type(typeFactory: CreateReviewMediaTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    object AddLarge : CreateReviewMediaUiModel {
        override val uri: String
            get() = ""
        override val uploadId: String
            get() = ""
        override val state: State
            get() = State.UPLOADED

        override fun areItemsTheSame(other: Any?): Boolean {
            return other is AddLarge
        }

        override fun areContentsTheSame(other: Any?): Boolean {
            return other is AddLarge
        }

        override fun getChangePayload(other: Any?): Bundle {
            return Bundle()
        }

        override fun type(typeFactory: CreateReviewMediaTypeFactory): Int {
            return typeFactory.type(this)
        }
    }

    enum class State {
        UPLOADING, UPLOADED, UPLOAD_FAILED
    }
}

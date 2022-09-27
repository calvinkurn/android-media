package com.tokopedia.review.feature.createreputation.presentation.uimodel.visitable

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.createreputation.presentation.adapter.typefactory.CreateReviewMediaTypeFactory

sealed interface CreateReviewMediaUiModel : Visitable<CreateReviewMediaTypeFactory> {

    companion object {
        const val PAYLOAD_MEDIA_STATE = "payloadMediaState"
        const val PAYLOAD_ADD_MEDIA_ENABLE_STATE = "payloadAddMediaEnableState"
    }

    val uri: String
    val uploadId: String
    val uploadBatchNumber: Int
    val finishUploadTimestamp: Long
    val state: State
    val message: String

    fun areItemsTheSame(other: Any?): Boolean
    fun areContentsTheSame(other: Any?): Boolean
    fun getChangePayload(other: Any?): Bundle

    fun isImage(): Boolean {
        return this is Image
    }

    fun isVideo(): Boolean {
        return this is Video
    }

    data class Image(
        override val uri: String,
        override val uploadId: String = "",
        override val uploadBatchNumber: Int,
        override val finishUploadTimestamp: Long = 0L,
        override val state: State,
        override val message: String = "",
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
        override val uploadBatchNumber: Int,
        override val finishUploadTimestamp: Long = 0L,
        override val state: State,
        override val message: String = "",
        val remoteUrl: String = ""
    ) : CreateReviewMediaUiModel {
        override fun areItemsTheSame(other: Any?): Boolean {
            return other is Video && uri == other.uri
        }

        override fun areContentsTheSame(other: Any?): Boolean {
            return other is Video && state == other.state
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

    data class AddSmall(
        override val uri: String = "",
        override val uploadId: String = "",
        override val uploadBatchNumber: Int = 0,
        override val finishUploadTimestamp: Long = 0L,
        override val state: State = State.UPLOADED,
        override val message: String = "",
        val enabled: Boolean
    ) : CreateReviewMediaUiModel {
        override fun areItemsTheSame(other: Any?): Boolean {
            return other is AddSmall
        }

        override fun areContentsTheSame(other: Any?): Boolean {
            return other is AddSmall && enabled == other.enabled
        }

        override fun getChangePayload(other: Any?): Bundle {
            return Bundle().apply {
                if (other !is AddSmall) return@apply
                if (enabled != other.enabled) {
                    putBoolean(PAYLOAD_ADD_MEDIA_ENABLE_STATE, other.enabled)
                }
            }
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
        override val uploadBatchNumber: Int
            get() = 0
        override val finishUploadTimestamp: Long
            get() = 0L
        override val state: State
            get() = State.UPLOADED
        override val message: String
            get() = ""

        override fun areItemsTheSame(other: Any?): Boolean {
            return other is AddLarge
        }

        override fun areContentsTheSame(other: Any?): Boolean {
            return this == other
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
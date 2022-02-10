package com.tokopedia.chatbot.data.uploadPolicy

import com.google.gson.annotations.SerializedName

data class ChatbotVODUploadPolicyResponse(
    @SerializedName("uploadpedia_policy")
    var uploadPolicy: UploadpediaPolicy
) {
    data class UploadpediaPolicy(
        @SerializedName("source_policy")
        var sourcePolicy: SourcePolicyResponse
    ) {
        data class SourcePolicyResponse(
            @SerializedName("host")
            var host: String,
            @SerializedName("timeout")
            var timeout: Int,
            @SerializedName("vod_policy")
            var vodPolicy: VODPolicy
        ) {
            data class VODPolicy(
                @SerializedName("max_file_size")
                var maxFileSize: Int,
                @SerializedName("allowed_ext")
                var allowedExtention: String,
                @SerializedName("simple_upload_size_threshold_mb")
                var simpleUploadSizeThresholdMB: Int,
                @SerializedName("big_upload_chunk_size_mb")
                var bigUploadChunkSizeMB: Int,
                @SerializedName("big_upload_max_concurrent")
                var bigUploadMaxConcurrent: Int,
                @SerializedName("timeout_transcode")
                var timeoutTranscode: Int,
                @SerializedName("retry_interval")
                var retryInterval: Int
            )
        }
    }
}


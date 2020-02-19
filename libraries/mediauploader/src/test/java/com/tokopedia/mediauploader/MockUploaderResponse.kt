package com.tokopedia.mediauploader

object MockUploaderResponse {

    val success = """
        {
            "data": {
                "upload_id": "819f5677-e6c7-49b9-872c-fe994c94dc9b"
            }
        }
    """.trimIndent()

    val error = """
        {
            "data": null
        }
    """.trimIndent()

}
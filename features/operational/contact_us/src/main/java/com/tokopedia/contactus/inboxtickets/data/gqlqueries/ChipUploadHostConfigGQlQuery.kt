package com.tokopedia.contactus.inboxtickets.data.gqlqueries

const val CHIP_UPLOAD_HOST_GQL = """{
  chipUploadHostConfig {
    status
    server_process_time
    data {
      generated_host {
        server_id
        token
        user_id
        upload_secure_host
      }
    }
    message_error
  }
}"""

package com.tokopedia.mediauploader.data.consts

object GraphQueryBuilder {

    private const val paramSourceId = "\$source"

    var mediaPolicy = """
        query dataPolicyQuery($paramSourceId: String) {
          uploadpedia_policy(source: $paramSourceId) {
            source_policy {
              host
              timeout
              image_policy {
                max_file_size
                max_res {
                  w
                  h
                }
                min_res {
                  w
                  h
                }
                allowed_ext
              }
            }
          }
        }
    """.trimIndent()

}
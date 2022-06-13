package com.tokopedia.mediauploader.common.data.consts

object GqlQueryBuilder {

    /**
     * This is used for param of grqphql query
     */
    private const val queryParamSourceId = "\$source"

    /**
     * This is used for param of query builder on each of use-case
     * the source produce `$source`. we need to take out first character,
     * so it should be return `source`.
     */

    // query param builder
    fun setSourceId(sourceId: String) = mapOf(
        queryParamSourceId.drop(1) to sourceId
    )

    var imagePolicy = """
        query dataPolicyQuery($queryParamSourceId: String) {
          uploadpedia_policy(source: $queryParamSourceId) {
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

    var videoPolicy = """
        query dataPolicyQuery($queryParamSourceId: String) {
          uploadpedia_policy(source: $queryParamSourceId) {
            source_policy {
              host
              timeout
              vod_policy {
                max_file_size
                allowed_ext
                simple_upload_size_threshold_mb
                big_upload_chunk_size_mb
                big_upload_max_concurrent
                timeout_transcode
                retry_interval
              }
            }
          }
        }
    """.trimIndent()

}
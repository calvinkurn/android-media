package com.tokopedia.graphql.data.model;

/**
 * Cache types which will be provide while creating request object for GraphQl usecase
 */
public enum CacheType {
    /* Caching will not perform in any case, (Data lookup will be try from server always, and no data will be saved into disk)*/
    NONE,

    /* Caching will perform always (Data lookup will be tried from disk first if found then return to collie,
    else lookup will promote to cloud and data will be saved always saved into disk in case of cloud lookup)*/
    CACHE_FIRST,

    /* Partially Caching will perform (Data lookup will be try from disk only, no data saved in this transaction into disk)*/
    CACHE_ONLY,

    /* Partially Caching will perform (Data lookup will be try from server always, however it will be saved the data into disk as well)*/
    ALWAYS_CLOUD,

    /* Same as ALWAYS_CLOUD. If failed when try from server, data lookup will be try from disk*/
    CLOUD_THEN_CACHE,
}

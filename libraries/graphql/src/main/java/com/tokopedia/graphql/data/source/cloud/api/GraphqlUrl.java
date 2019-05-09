package com.tokopedia.graphql.data.source.cloud.api;

import com.tokopedia.config.url.TokopediaUrl;

/**
 * @author by yfsx on 19/07/18.
 */
public class GraphqlUrl {
    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getGQL();
}

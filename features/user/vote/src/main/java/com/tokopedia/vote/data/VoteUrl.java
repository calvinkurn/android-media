package com.tokopedia.vote.data;

import com.tokopedia.config.url.TokopediaUrl;

public class VoteUrl {
    public static String BASE_URL = TokopediaUrl.Companion.getInstance().getCHAT();

    public static final String SEND_VOTE_V1 = "/gmf/api/v1/poll/{poll_id}/vote";
    public static final String SEND_VOTE = "/gmf/api/v2/poll/{poll_id}/vote";
    public static final String PATH_POLL_ID = "poll_id";
}
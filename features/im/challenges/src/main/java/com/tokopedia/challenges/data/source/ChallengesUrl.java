package com.tokopedia.challenges.data.source;

import com.tokopedia.network.utils.AuthUtil;

public class ChallengesUrl {
    //Base Url
    public static String INDI_DOMAIN = "https://api.indi.com/v1/";
    public static String API_KEY = AuthUtil.KEY.INDI_API_KEY;
    public static String CHANNEL_ID = "d295e3d1-ef65-4783-adc1-e77168561a1a";
    public static String CHALLENGE_ID = "%s";

    public interface Auth {
        String ACCESS_TOKEN = "auth/token?client_id=08b1486e-536b-4b65-b682-fe42a4c00cd4&client_secret=Je781oTaoxML9J4DJ8QrV72OCdlkLXTVEV2R";
    }

    public interface MANAGE {
        String USER_MAP = "manage/users/map";
        String SHARE_URL_MAP = "manage/submission/share/url";
    }

    public interface PRIVATE {
        String PRIVATE = "private/";
        String ME = "private/me";
        String OPEN_CHALLENGES = "private/" + CHANNEL_ID + "/challenges/open?start=0&size=50";
        String PAST_CHALLENGES = "private/" + CHANNEL_ID + "/challenges/closed?start=0&size=50";
        String CHALLENGES_SUBMISSIONS = "private/" + CHALLENGE_ID + "/submissions";
        String SUBMISSIONS_LIKE = "private/%s/track/like";
        String SUBMISSIONS_UNLIKE = "private/%s/track/unlike";
        String CHALLENGES_DETAILS = "private/" + CHALLENGE_ID ;
        String BUZZPOINT_EVENT = "private/%s/track/view";
        String SUBMISSIONS_DETAILS = "private/";
        String CHALLENGE_WINNERS = "private/" + CHALLENGE_ID + "/winners";



        interface Upload {
            String PRIVATE_CHANNENGE_ID = PRIVATE + CHALLENGE_ID;
            String CHALLENGE_SETTING = PRIVATE_CHANNENGE_ID + "/upload/settings";
            String CHALLENGE_TERMS = PRIVATE_CHANNENGE_ID + "/upload/terms";
            String CHALLENGE_INTIALIZE_MULTIPART = PRIVATE_CHANNENGE_ID + "/upload/initialize";
            String CHALLENGE_GET_NEXT_PART = PRIVATE_CHANNENGE_ID + "/upload/getnextpart";
            String TERMS_N_CONDITIONS = "private/%s/upload/terms";
            String DELETE_POST = "private/%s/upload/delete";

        }

    }

    public interface Me {
        String SUBMISSIONS = "private/me/submissions?sort=recent";
        String SUBMISSIONS_IN_CHALLENGE = "private/%s/submissions/me?sort=recent";

    }

    public interface AppLink {
        String CHALLENGES_HOME = "tokopedia://challenges";
        String CHALLENGES_DETAILS = "tokopedia://challenges/challenge/{challenge-id}";
        String SUBMISSION_DETAILS = "tokopedia://challenges/submission/{submission-id}";
    }

}
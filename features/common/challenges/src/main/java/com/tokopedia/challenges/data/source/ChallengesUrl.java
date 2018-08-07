package com.tokopedia.challenges.data.source;

public class ChallengesUrl {
    //Base Url
    public static String INDI_DOMAIN = "https://api.indi.com/v1/";
    public static String API_KEY = "EERIxwXF644c1E1To5puL8xNP5PvLHSv240PyNYf";

    public interface Auth {
        String ACCESS_TOKEN = "auth/token?client_id=08b1486e-536b-4b65-b682-fe42a4c00cd4&client_secret=Je781oTaoxML9J4DJ8QrV72OCdlkLXTVEV2R";
    }

    public interface MANAGE {
        String USER_MAP = "manage/users/map";
    }

    public interface PRIVATE {
        String ME = "private/me";
    }
}
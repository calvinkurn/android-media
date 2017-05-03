package com.tokopedia.core.var;

import java.util.Arrays;
import java.util.List;

/**
 * Created by stevenfredian on 1/16/17.
 */

public class FacebookContainer {
    public static final List<String> readPermissions = Arrays.asList("public_profile", "email", "user_birthday");
    public static final List<String> writePermissions = Arrays.asList("publish_actions");
}

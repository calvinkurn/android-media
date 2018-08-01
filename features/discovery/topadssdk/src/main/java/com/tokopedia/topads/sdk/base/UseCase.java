package com.tokopedia.topads.sdk.base;

/**
 * @author by errysuprayogi on 3/31/17.
 */

public abstract class UseCase<Param, View> {

    public abstract void setConfig(Config config);

    public abstract void execute(Param requestParams, View view);

    public abstract void unsubscribe();

}

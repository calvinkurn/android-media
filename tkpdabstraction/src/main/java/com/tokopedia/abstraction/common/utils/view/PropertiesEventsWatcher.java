package com.tokopedia.abstraction.common.utils.view;

import android.view.View;

import rx.Observable;
import rx.functions.Action1;


/**
 * Created by stevenfredian on 7/13/17.
 */

public final class PropertiesEventsWatcher {

    // no instances of helper class
    private PropertiesEventsWatcher() { }

    /**
     * Updates the <em>enabled</em> property of a view for each event received.
     */
    public static EnabledProperty enabledFrom(View view) {
        return new EnabledProperty(view);
    }

    public static class EnabledProperty implements Action1<Boolean> {
        private final View view;

        private EnabledProperty(View view) {
            this.view = view;
        }

        public void set(Observable<Boolean> observable) {
            observable.subscribe(this,  Throwable::printStackTrace);
        }

        @Override public void call(Boolean enabled) {
            view.setEnabled(enabled);
        }
    }
}
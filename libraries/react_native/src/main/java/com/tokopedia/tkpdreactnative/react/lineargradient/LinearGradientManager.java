package com.tokopedia.tkpdreactnative.react.lineargradient;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

public class LinearGradientManager extends SimpleViewManager<LinearGradientView> {

    private static final String REACT_MODULE_NAME = "BVLinearGradient";
    private static final String PROP_COLORS = "colors";
    private static final String PROP_LOCATIONS = "locations";
    private static final String PROP_START_POS = "startPoint";
    private static final String PROP_END_POS = "endPoint";
    private static final String PROP_USE_ANGLE = "useAngle";
    private static final String PROP_ANGLE_CENTER = "angleCenter";
    private static final String PROP_ANGLE = "angle";
    private static final String PROP_BORDER_RADII = "borderRadii";

    @Override
    public String getName() {
        return REACT_MODULE_NAME;
    }

    @Override
    protected LinearGradientView createViewInstance(ThemedReactContext context) {
        return new LinearGradientView(context);
    }

    @ReactProp(name = PROP_COLORS)
    public void setColors(LinearGradientView gradientView, ReadableArray colors) {
        gradientView.setColors(colors);
    }

    @ReactProp(name = PROP_LOCATIONS)
    public void setLocations(LinearGradientView gradientView, ReadableArray locations) {
        if (locations != null) {
            gradientView.setLocations(locations);
        }
    }

    @ReactProp(name = PROP_START_POS)
    public void setStartPosition(LinearGradientView gradientView, ReadableArray startPos) {
        gradientView.setStartPosition(startPos);
    }

    @ReactProp(name = PROP_END_POS)
    public void setEndPosition(LinearGradientView gradientView, ReadableArray endPos) {
        gradientView.setEndPosition(endPos);
    }

    @ReactProp(name = PROP_USE_ANGLE, defaultBoolean = false)
    public void setUseAngle(LinearGradientView gradientView, boolean useAngle) {
        gradientView.setUseAngle(useAngle);
    }

    @ReactProp(name = PROP_ANGLE_CENTER)
    public void setAngleCenter(LinearGradientView gradientView, ReadableArray in) {
        gradientView.setAngleCenter(in);
    }

    @ReactProp(name = PROP_ANGLE, defaultFloat = 45.0f)
    public void setAngle(LinearGradientView gradientView, float angle) {
        gradientView.setAngle(angle);
    }

    // temporary solution until following issue is resolved:
    // https://github.com/facebook/react-native/issues/3198
    @ReactProp(name = PROP_BORDER_RADII)
    public void setBorderRadii(LinearGradientView gradientView, ReadableArray borderRadii) {
        gradientView.setBorderRadii(borderRadii);
    }
}

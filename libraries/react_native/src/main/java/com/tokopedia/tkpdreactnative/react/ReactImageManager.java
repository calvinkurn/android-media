package com.tokopedia.tkpdreactnative.react;

import javax.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeControllerBuilder;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.views.image.GlobalImageLoadListener;
import com.facebook.react.views.image.ImageResizeMode;
import com.facebook.react.views.image.ReactImageView;

public class ReactImageManager extends SimpleViewManager<ReactImageView> {
    private final @Nullable Object mCallerContext;
    private @Nullable AbstractDraweeControllerBuilder mDraweeControllerBuilder;
    private @Nullable GlobalImageLoadListener mGlobalImageLoadListener;

    @Override
    public String getName() {
        return "RCTImageView";
    }

    @Override
    protected ReactImageView createViewInstance(ThemedReactContext reactContext) {
        return new ReactImageView(reactContext, getDraweeControllerBuilder(), mGlobalImageLoadListener, getCallerContext());
    }

    public Object getCallerContext() {
        return mCallerContext;
    }

    public AbstractDraweeControllerBuilder getDraweeControllerBuilder() {
        if (mDraweeControllerBuilder == null) {
            mDraweeControllerBuilder = Fresco.newDraweeControllerBuilder();
        }
        return mDraweeControllerBuilder;
    }

    @ReactProp(name = "src")
    public void setSrc(ReactImageView view, @Nullable ReadableArray sources) {
        view.setSource(sources);
    }

    @ReactProp(name = "borderRadius", defaultFloat = 0f)
    public void setBorderRadius(ReactImageView view, float borderRadius) {
        view.setBorderRadius(borderRadius);
    }

    @ReactProp(name = ViewProps.RESIZE_MODE)
    public void setResizeMode(ReactImageView view, @Nullable String resizeMode) {
        view.setScaleType(ImageResizeMode.toScaleType(resizeMode));
    }

    public ReactImageManager(
            AbstractDraweeControllerBuilder draweeControllerBuilder,
            @Nullable GlobalImageLoadListener globalImageLoadListener,
            Object callerContext) {
        mDraweeControllerBuilder = draweeControllerBuilder;
        mGlobalImageLoadListener = globalImageLoadListener;
        mCallerContext = callerContext;
    }

    public ReactImageManager() {
        // Lazily initialize as FrescoModule have not been initialized yet
        mDraweeControllerBuilder = null;
        mCallerContext = null;
    }
}

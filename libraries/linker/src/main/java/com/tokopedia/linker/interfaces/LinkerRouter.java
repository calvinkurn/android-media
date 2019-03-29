package com.tokopedia.linker.interfaces;

public interface LinkerRouter {
    boolean getBooleanRemoteConfig(String key, boolean defaultValue);
    String getDesktopLinkGroupChat();
}

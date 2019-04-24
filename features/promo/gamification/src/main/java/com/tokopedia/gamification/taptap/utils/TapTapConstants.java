package com.tokopedia.gamification.taptap.utils;

public class TapTapConstants {

    public interface TokenState {
        String STATE_EMPTY = "empty";
        String STATE_LOBBY = "lobby";
        String STATE_CRACK_LIMITED = "cracklimited";
        String STATE_CRACK_UNLIMITED = "crackunlimited";
    }

    public interface ButtonColor {
        String WHITE = "white";
        String GREEN = "green";
        String OUTLINE = "outline";
    }

    public interface ButtonType {
        String REDIRECT = "redirect";
        String EXIT = "exit";
        String PLAY_WITH_POINTS = "playwithpoints";
    }
}

package com.tokopedia.topchat.chattemplate.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.topchat.chattemplate.data.factory.EditTemplateChatFactory;
import com.tokopedia.topchat.chattemplate.data.factory.TemplateChatFactory;
import com.tokopedia.topchat.chattemplate.data.mapper.EditTemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepository;
import com.tokopedia.topchat.chattemplate.data.repository.EditTemplateRepositoryImpl;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepository;
import com.tokopedia.topchat.chattemplate.data.repository.TemplateRepositoryImpl;
import com.tokopedia.topchat.common.chat.ChatService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by stevenfredian on 9/14/17.
 */

@Module
public class TemplateChatModule {


    @TemplateChatScope
    @Provides
    AnalyticTracker provideAnalyticTracker(
            @ApplicationContext Context context) {
        return ((AbstractionRouter) context).getAnalyticTracker();
    }

    @TemplateChatScope
    @Provides
    ChuckInterceptor provideChuckInterceptor(
            @ApplicationContext Context context) {
        return new ChuckInterceptor(context);
    }

    @TemplateChatScope
    @Provides
    TemplateChatFactory provideTemplateChatFactory(
            ChatService chatService,
            TemplateChatMapper templateChatMapper) {
        return new TemplateChatFactory(templateChatMapper, chatService);
    }

    @TemplateChatScope
    @Provides
    EditTemplateChatFactory provideEditTemplateChatFactory(
            ChatService chatService,
            EditTemplateChatMapper templateChatMapper) {
        return new EditTemplateChatFactory(templateChatMapper, chatService);
    }

    @TemplateChatScope
    @Provides
    TemplateRepository provideTemplateRepository(TemplateChatFactory templateChatFactory) {
        return new TemplateRepositoryImpl(templateChatFactory);
    }

    @TemplateChatScope
    @Provides
    EditTemplateRepository provideEditTemplateRepository(EditTemplateChatFactory templateChatFactory) {
        return new EditTemplateRepositoryImpl(templateChatFactory);
    }

}

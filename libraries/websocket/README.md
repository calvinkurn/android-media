# Websocket

### Download

```
dependencies {
    implementation project(rootProject.ext.libraries.websocket)
}
```

### How to use
```
val websocketSubscriber = object : WebSocketSubscriber() {

    // override this method to check wether websocket is open
    override fun onOpen(webSocket: WebSocket) {
    }

    // override this method to handle when websocket onreconnect 
    override fun onReconnect() {
    }

    // override this method to handle when websocket onclose 
    override fun onClose() {
    }

    // override this method to retrieve string response 
    override fun onMessage(text: String) {
    }


    // override this method to retrieve bytestring response 
    override fun onMessage(byteString: ByteString) {
    }

    // override this method to retrieve WebSocketResponse response
    // type: WebSocketResponse(type: String, code: Int, jsonObject: JsonObject?)
    override fun onMessage(webSocketResponse: WebSocketResponse) {
    }

    // override this method to handle when websocket onerror 
    override fun onError(e: Throwable) {
    }

}

val websocket = RxWebSocket[
                "{fill the url here - String}",
                "{fill the access token here - String}",
                "{fill the delay by default is 5000ms - Int}",
                "{fill the ping interval by default is 10000L ms - Long}",
                "{fill the max entries by default is 3 times - Int}"
        ]

val subsribeWebsocket = websocket?.subscribe(websocketSubscriber)

var subscription: CompositeSubscription = CompositeSubscription()
subscription.add(subsribeWebsocket)
```

## Modules that implements this library
- features/content/play
- features/user/chat_common
- features/user/chatbot
- features/user/topchat


## Author
Steven Fredian (@stevenfredian)

#### Contributors
Meyta (@mzennis)


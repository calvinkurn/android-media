# Interface layer

- init(config: Configuration)
    1. configurable on/off iris
    2. init database
    3. start worker
        - configurable batching period (default value: 1min)
        - configurable batching size (default value: 50rows)

- trackEvent(data: Map<String, Object>)
    1. append data
        - append iris session id
    2. save to db

- setUserId(id: String)
    - save user Id to shared preference
    - delete user Id if null

# Background layer

- fetchData(rowCount: int)
    1. group by sessionId
    2. sort by date
    3. get {rowCount} data, sorted from the oldest 

- sendData(data: JsonObject)
    1. success
        - delete data
        - trigger timer for next worker
    2. failed
        - trigger timer for next worker

# Database schema

## tracking table
id | timestamp | eventdata | sessionid
1 | 10234234 | "{eventName:asdf,app_version:2.3,device_id:12341234,platform:android,version:1}" | 1a2b3c
2 | 10234234 | "{en:asdf,av:2.3,di:12341234,p:android,v:2,uid:123123123}" | 1a2b3c
3 | 10234234 | "{en:asdf,av:2.3,di:12341234,p:android,v:2}" | 1a2b3c

## session table
id | sessionId | data
1 | 1a2b3c | {userId: 123, fingerprint: lkjJsdfsFOsifsoaIosfa, deviceId: 12312-123123-123213-21312312-12312} 
    
import { SessionModule } from 'NativeModules'
import { BASE_API_DIGITAL, BASE_API_MARKETPLACE } from './api'



export const getEnv = () => {
    return SessionModule.getEnv()
        .then(res => { return res })
        .catch(err => console.log(err))
}

export const getBaseAPI = (env) => {
    let data_api = {}
    
    if (env === 'production'){
        const data_api = {
            digital: `${BASE_API_DIGITAL.production}`,
            marketplace: `${BASE_API_MARKETPLACE.production}`
        }
        return data_api
    } else {
        const data_api = {
            digital: `${BASE_API_DIGITAL.staging}`,
            marketplace: `${BASE_API_MARKETPLACE.staging}`
        }
        return data_api
    }
  }

export * from './product'
export * from './shop'
export * from './cart'
export * from './checkout'
export * from './bank'
export * from './payment'



//  ==================== Clear the Redux ===================== //
export const RELOAD_STATE = 'RELOAD_STATE'
export const reloadState = (state, action) => {
  console.log('reload state ', state)
  return {
    type: RELOAD_STATE
  }
}
import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/Store'
// import PaymentBank from './components/PaymentBank'
// import Payment from './components/Payment'
// import PaymentProcessing from './components/PaymentProcessing'
// import PaymentInvoice from './components/PaymentInvoice'
// import TransactionHistory from './components/TransactionHistory'
// import PasswordPopup from './components/PasswordPopup'
import POS from './components/POS'
// import CartContainer from './containers/CartContainer'
// import { StackNavigator } from 'react-navigation';

// // // TODO: FOR UI testing purpose
// const App = StackNavigator({
//   POS: {screen: POS},
//   BankSelection : { screen: PaymentBank },
//   Payment: { screen: Payment },
//   PaymentProcessing: {screen: PaymentProcessing},
//   PaymentInvoice: {screen: PaymentInvoice},
//   TransactionHistory: {screen: TransactionHistory},
//   PasswordPopup: {screen: PasswordPopup}
// }, {headerMode: 'none'});


class Root extends Component {
    componentWillMount(){
        console.log(this.props)
    }

    render() {
        return (
            <Provider store={store}>
                <POS screenProps={this.props.data} />
            </Provider>
        )
    }
}
export default Root
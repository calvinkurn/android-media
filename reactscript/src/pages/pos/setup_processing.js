import React, { Component } from 'react'
import { Provider } from 'react-redux'
import store from './store/Store'
// import PaymentBank from './components/PaymentBank'
// import Payment from './components/Payment'
// import PaymentProcessing from './components/PaymentProcessing'
// import PaymentInvoice from './components/PaymentInvoice'
// import TransactionHistory from './components/TransactionHistory'
// import PasswordPopup from './components/PasswordPopup'
// import POS from './components/POS'
// import CartContainer from './containers/CartContainer'
// import { StackNavigator } from 'react-navigation';
import Setup_processing from './setup_processing'
import Setup_invoice from './setup_paymentinvoice'

// // // TODO: FOR UI testing purpose
const App = StackNavigator({
    Setup_processing: {screen: Setup_processing},
    Setup_invoice : { screen: Setup_invoice }
}, {headerMode: 'none'});


class Root extends Component {
    componentWillMount() {
        console.log(this.props)
    }

    render() {
        return (
            <Provider store={store}>
                <App />
            </Provider>
        )
    }
}
export default Root